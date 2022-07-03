package org.foi.nwtis.ihuzjak.aplikacija_2.dretve;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.foi.nwtis.ihuzjak.aplikacija_2.podaci.AerodromPracen;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

public class PreuzimanjeRasporedaAerodroma extends Thread {

	Konfiguracija konfig = null;
	
	long ciklusVrijeme;
	int ciklusKorekcija;
	long preuzimanjeOdmak;
	long preuzimanjePauza;
	long preuzimanjeOd;
	long preuzimanjeDo;
	long preuzimanjeVrijeme;
	String OpenSkyNetworkKorisnik;
	String OpenSkyNetworkLozinka;
	
	long stvarniBrojac;
	long virtualniBrojac;
	
	long vrijemeObrade;
	long pocetakObrade;
	long zavrsetakObrade;
	long vrijemeSpavanja;
	long trenutnoVrijemeObrade;
	
	long vrijemePocinjanjaSpavanja;
	long vrijemeZavrsetkaSpavanja;
	long vrijemeKorekcijeZaSpavanje;
	
	int status = 0;
	
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
	
	OSKlijent oSKlijent;
	
	public PreuzimanjeRasporedaAerodroma(Konfiguracija konfig){
		this.konfig = konfig;
	}
	
	@Override
	public synchronized void start() {
		oSKlijent = new OSKlijent(OpenSkyNetworkKorisnik, OpenSkyNetworkLozinka);
		ucitajKonfiguracijskePodatke(konfig);
		trenutnoVrijemeObrade = preuzimanjeOd;
		vrijemeKorekcijeZaSpavanje = 0;
		stvarniBrojac = 0;
		virtualniBrojac = 0;
		super.start();
	}
	
	private void ucitajKonfiguracijskePodatke(Konfiguracija konfig) {
		ciklusVrijeme = Long.parseLong(konfig.dajPostavku("ciklus.vrijeme")) * 1000;
		ciklusKorekcija = Integer.parseInt(konfig.dajPostavku("ciklus.korekcija"));
		preuzimanjeOdmak = Long.parseLong(konfig.dajPostavku("preuzimanje.odmak")) * 24 * 60 * 60 * 1000;
		preuzimanjePauza = Long.parseLong(konfig.dajPostavku("preuzimanje.pauza"));
		try {
			Timestamp TpreuzimanjeOd = new Timestamp(((Date)sdf2.parse(konfig.dajPostavku("preuzimanje.od")))
					.getTime());
			preuzimanjeOd = TpreuzimanjeOd.getTime();
			Timestamp TpreuzimanjeDo = new Timestamp(((Date)sdf2.parse(konfig.dajPostavku("preuzimanje.do")))
					.getTime());
			preuzimanjeDo = TpreuzimanjeDo.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		preuzimanjeVrijeme = Long.parseLong(konfig.dajPostavku("preuzimanje.vrijeme")) * 60 * 60 * 1000;
		OpenSkyNetworkKorisnik = konfig.dajPostavku("OpenSkyNetwork.korisnik");
		OpenSkyNetworkLozinka = konfig.dajPostavku("OpenSkyNetwork.lozinka");	
	}

	@Override
	public void run() {
		
		postaviDriver();
		
		while (status == 0 && trenutnoVrijemeObrade < preuzimanjeDo) {
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			pocetakObrade = timestamp.getTime();
			if (trenutnoVrijemeObrade + preuzimanjeOdmak < pocetakObrade) {
				trenutnoVrijemeObrade += preuzimanjeVrijeme;
			} else {
				try {
					Thread.sleep(86000000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				stvarniBrojac++;
				virtualniBrojac += 86000000 / ciklusVrijeme;
				continue;
			}
			
			List<AerodromPracen> aerodromi = new ArrayList<>();
			String url = konfig.dajPostavku("server.database") + konfig.dajPostavku("user.database");
			
			dohvatiPraceneAerodrome(aerodromi, url);
	        
			for (AerodromPracen aerodrom : aerodromi) {
				upisiPolaskeUBazu(url, aerodrom);
				upisiDolaskeUBazu(url, aerodrom);
				spavajPauza();
			}
			postaviBrojaceKraj();
			spavajIOdradiKraj();
		}
	}

	private void upisiPolaskeUBazu(String url, AerodromPracen aerodrom) {
		
		try {
			List<AvionLeti> avioniPolasci = oSKlijent.getDepartures(aerodrom.getIdent(), trenutnoVrijemeObrade/1000,
					trenutnoVrijemeObrade/1000 + preuzimanjeVrijeme/1000);
			
			if (avioniPolasci != null) {
		        try (Connection con = DriverManager.getConnection(url,konfig.dajPostavku("user.username"),
		        		konfig.dajPostavku("user.password")))	     
		        {
					for (AvionLeti a : avioniPolasci) {
						if (a.getEstArrivalAirport() != null) {
							String upitPolasci = stvoriUpitZaPolaske(a);
							PreparedStatement pstmt = con.prepareStatement(upitPolasci);
				        	pstmt.execute();
				        	pstmt.close();
						}
					}
		        } catch(SQLException ex) {
		        	System.out.println("INFO INTERNAL APK2 [B4] >> SQL pogreška pri upisu podataka u tablicu"
		        			+ " AERODROMI_POLASCI!");
		        }
			}
		} catch (NwtisRestIznimka e) {
			upisiProblemUBazu(url, aerodrom, e);
		}
	}

	private String stvoriUpitZaPolaske(AvionLeti a) {
		
		String upitPolasci = "INSERT INTO AERODROMI_POLASCI"
			+ " (icao24, firstseen, estdepartureairport, lastseen, estarrivalairport,"
			+ " callsign,"
			+ " estdepartureairporthorizdistance, estdepartureairportvertdistance,"
			+ " estarrivalairporthorizdistance, estarrivalairportvertdistance,"
			+ " departureairportcandidatescount, arrivalairportcandidatescount, stored)"
			+ " VALUES ("
			+ "'" + a.getIcao24() + "', "
			+ a.getFirstSeen() + ", "
			+ "'" + a.getEstDepartureAirport() + "', "
			+ a.getLastSeen() + ", "
			+ "'" + a.getEstArrivalAirport() + "', "
			+ "'" + a.getCallsign() + "', "
			+ a.getEstDepartureAirportHorizDistance() + ", "
			+ a.getEstDepartureAirportVertDistance() + ", "
			+ a.getEstArrivalAirportHorizDistance() + ", "
			+ a.getEstArrivalAirportVertDistance() + ", "
			+ a.getDepartureAirportCandidatesCount() + ", "
			+ a.getArrivalAirportCandidatesCount() + ", "
			+ "CURRENT_TIMESTAMP);";
		return upitPolasci;
	}

	private void upisiDolaskeUBazu(String url, AerodromPracen aerodrom) {
		
		try {
			List<AvionLeti> avioniDolasci = oSKlijent.getArrivals(aerodrom.getIdent(), trenutnoVrijemeObrade/1000,
					trenutnoVrijemeObrade/1000 + preuzimanjeVrijeme/1000);
			
			if (avioniDolasci != null) {
		        try (Connection con = DriverManager.getConnection(url,konfig.dajPostavku("user.username"),
		        		konfig.dajPostavku("user.password")))	     
		        {
					for (AvionLeti a : avioniDolasci) {
						if (a.getEstDepartureAirport() != null) {
							String upitDolasci = stvoriUpitDolasci(a);
							PreparedStatement pstmt = con.prepareStatement(upitDolasci);
				        	pstmt.execute();
				        	pstmt.close();
						}
					}
		        } catch(SQLException ex) {
		        	System.out.println("INFO INTERNAL APK2 [B3] >> SQL pogreška pri upisu podataka u tablicu"
		        			+ " AERODROMI_DOLASCI!");
		        }
			}
		} catch (NwtisRestIznimka e) {
			upisiProblemUBazu(url, aerodrom, e);
		}
	}

	private void postaviDriver() {
		try {
		     Class.forName("org.hsqldb.jdbc.JDBCDriver");
		 } catch (Exception e) {
			 System.out.println("INFO INTERNAL APK2 [B1] >> Pogreška pri postavljanju upravitelja baze podataka!");
		 }
	}
	
	private void dohvatiPraceneAerodrome(List<AerodromPracen> aerodromi, String url) {
		
		String upitPraceni = "SELECT * FROM  AERODROMI_PRACENI;";
	    try (Connection con = DriverManager.getConnection(url,konfig.dajPostavku("user.username"),
	    		konfig.dajPostavku("user.password"));
		     PreparedStatement pstmt = con.prepareStatement(upitPraceni);
	    	 ResultSet rs = pstmt.executeQuery())
	    {
	            while (rs.next()) {
	            	AerodromPracen a = new AerodromPracen("","");
	                a.setIdent(rs.getString("ident"));
	                aerodromi.add(a);
	            }

	    } catch(SQLException ex) {
	    	System.out.println("INFO INTERNAL APK2 [B2] >> Pogreška pri dohvaćanju podataka iz tablice AERODROMI_PRACENI!");
	    }
	}
	
	private String stvoriUpitDolasci(AvionLeti a) {
		
		String upitDolasci = "INSERT INTO AERODROMI_DOLASCI"
			+ " (icao24, firstseen, estdepartureairport, lastseen, estarrivalairport,"
			+ " callsign,"
			+ " estdepartureairporthorizdistance, estdepartureairportvertdistance,"
			+ " estarrivalairporthorizdistance, estarrivalairportvertdistance,"
			+ " departureairportcandidatescount, arrivalairportcandidatescount, stored)"
			+ " VALUES ("
			+ "'" + a.getIcao24() + "', "
			+ a.getFirstSeen() + ", "
			+ "'" + a.getEstDepartureAirport() + "', "
			+ a.getLastSeen() + ", "
			+ "'" + a.getEstArrivalAirport() + "', "
			+ "'" + a.getCallsign() + "', "
			+ a.getEstDepartureAirportHorizDistance() + ", "
			+ a.getEstDepartureAirportVertDistance() + ", "
			+ a.getEstArrivalAirportHorizDistance() + ", "
			+ a.getEstArrivalAirportVertDistance() + ", "
			+ a.getDepartureAirportCandidatesCount() + ", "
			+ a.getArrivalAirportCandidatesCount() + ", "
			+ "CURRENT_TIMESTAMP);";
		return upitDolasci;
	}
	
	private void upisiProblemUBazu(String url, AerodromPracen aerodrom, NwtisRestIznimka e) {
		
		String ae = aerodrom.getIdent();
		String msg = e.getMessage();
		try (Connection con = DriverManager.getConnection(url,konfig.dajPostavku("user.username"),
				konfig.dajPostavku("user.password")))	     
		{
		String upitProblem = "INSERT INTO AERODROMI_PROBLEMI"
				+ " (ident, description, stored)"
				+ " VALUES ("
				+ "'" + ae + "', "
				+ "'" + msg + "', "
				+ "CURRENT_TIMESTAMP);";
		PreparedStatement pstmt = con.prepareStatement(upitProblem);
		pstmt.execute();
		pstmt.close();
		} catch(SQLException ex) {
			System.out.println("INFO INTERNAL APK2 [B5] >> Pogreška pri unosu podataka u tablicu AERODROMI_PROBLEMI!");
		}
	}
	
	private void spavajPauza() {
		try {
			Thread.sleep(preuzimanjePauza);
		} catch (InterruptedException e) {
			System.out.println("INFO INTERNAL APK2 [S2] >> Prekinuta pauza dretve preuzimanja!");
		}
	}
	
	private void postaviBrojaceKraj() {
		
		Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
		zavrsetakObrade = timestamp2.getTime();
		
		vrijemeObrade = zavrsetakObrade - pocetakObrade;
		if (vrijemeObrade < ciklusVrijeme) {
			vrijemeSpavanja = ciklusVrijeme - vrijemeObrade;
			stvarniBrojac++;
			virtualniBrojac++;
		} else {
			double potrosenoCiklusa = vrijemeObrade / ciklusVrijeme;
			int potrosenoCiklusaINT = (int) potrosenoCiklusa + 1;
			vrijemeSpavanja = (ciklusVrijeme * potrosenoCiklusaINT) - vrijemeObrade;
			stvarniBrojac++;
			virtualniBrojac += potrosenoCiklusaINT;
		}
	}
	
	private void spavajIOdradiKraj() {
		
		if (stvarniBrojac % ciklusKorekcija == 0) {
			vrijemeSpavanja -= vrijemeKorekcijeZaSpavanje;
			vrijemeKorekcijeZaSpavanje = 0;
		}
		Timestamp timestamp3 = new Timestamp(System.currentTimeMillis());
		vrijemePocinjanjaSpavanja = timestamp3.getTime();
		try {
			Thread.sleep(vrijemeSpavanja);
		} catch (InterruptedException e) {
			System.out.println("INFO INTERNAL APK2 [S2] >> Prekinuto vrijeme spavanja dretve prije sljedećeg preuzimanja!");
		}
		Timestamp timestamp4 = new Timestamp(System.currentTimeMillis());
		vrijemeZavrsetkaSpavanja = timestamp4.getTime();
		vrijemeKorekcijeZaSpavanje += vrijemePocinjanjaSpavanja - vrijemeZavrsetkaSpavanja;
		vrijemeKorekcijeZaSpavanje -= vrijemeSpavanja;
		if (vrijemeKorekcijeZaSpavanje < 0) {
			vrijemeKorekcijeZaSpavanje = 0;
		}
	}

	@Override
	public void interrupt() {
		status++;
		super.interrupt();
	}
}
