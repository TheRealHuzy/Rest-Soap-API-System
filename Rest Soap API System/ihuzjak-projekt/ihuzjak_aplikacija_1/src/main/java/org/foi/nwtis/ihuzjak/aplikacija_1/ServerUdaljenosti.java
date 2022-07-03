package org.foi.nwtis.ihuzjak.aplikacija_1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.podaci.Aerodrom;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ServerUdaljenosti {

	int port;
	int maksCekaca;
	String SAerodromaAdresa;
	int SAerodromaPort;
	int maksCekanje;
	int maksDretve;
	List<Aerodrom> aerodromi = new ArrayList<>();
	String status = "^STATUS$";
	String quit = "^QUIT$";
	String init = "^INIT$";
	String load = "^LOAD \\[\\{.+\\}\\]$";
	String distanceIcaoIcao = "^DISTANCE ([A-Z]{4}) ([A-Z]{4})$";
	String clear = "^CLEAR$";
	static String argument = "^(\\w+).(txt|bin|json|xml)$";
	String odgovorAero = "";
	private static final DecimalFormat df = new DecimalFormat("0");
	static public Konfiguracija konfig = null;
	int statusServera = 0;
	boolean serverRadi = true;
	ServerSocket ss;
	List<DretvaUdaljenosti> dretve = new ArrayList<DretvaUdaljenosti>();

	public static void main(String[] args) {
		
		if(args.length != 1 || !ispitajArgument(args[0])) {
			System.out.println("ERROR 14 INTERNAL >> Broj argumenata nije 1 ili je neispravan!");
			return;
		}
		ucitavanjePodataka(args[0]);
		if(konfig == null) {
			System.out.println("ERROR 14 INTERNAL >> Problem s konfiguracijom.");
			return;
		}
		if(!provjeriKonfig()) {
			System.out.println("ERROR 14 INTERNAL >> Problem s nepotpunim konfiguracijskim podacima!");
			return;
		}
		
		int port = Integer.parseInt(konfig.dajPostavku("port"));
		int maksCekaca = Integer.parseInt(konfig.dajPostavku("maks.cekaca"));
		int sUdaljenostiPort = Integer.parseInt(konfig.dajPostavku("server.aerodroma.port"));
		String sUdaljenostiAdresa = konfig.dajPostavku("server.aerodroma.adresa");
		int maksCekanje = Integer.parseInt(konfig.dajPostavku("maks.cekanje"));
		int maksDretve = Integer.parseInt(konfig.dajPostavku("maks.dretve"));
				
		ServerUdaljenosti sm = new ServerUdaljenosti(port, maksCekaca, sUdaljenostiAdresa,
				sUdaljenostiPort, maksCekanje, maksDretve);
		
		if(sm.ispitajPort(port)) {
			return;
		}
		sm.obradaZahtjeva();
	}

	private static boolean ispitajArgument(String arg) {
		
		Pattern pArgument = Pattern.compile(argument);
		Matcher mArgument = pArgument.matcher(arg.toString());
		if (mArgument.matches()) {
			return true;
		}
		return false;
	}

	public ServerUdaljenosti(int port, int maksCekaca, String sUdaljenostiAdresa,
			int sAerodromaPort, int maksCekanje, int maksDretve) {
		super();
		this.port = port;
		this.maksCekaca = maksCekaca;
		SAerodromaAdresa = sUdaljenostiAdresa;
		SAerodromaPort = sAerodromaPort;
		this.maksCekanje = maksCekanje;
		this.maksDretve = maksDretve;
	}
	
	private static void ucitavanjePodataka(String nazivDatoteke) {
		
		try {
			konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
		} catch (NeispravnaKonfiguracija e) {
			System.out.println("ERROR 14 INTERNAL >> Greška pri učitavanju konfiguracijske datoteke!");
		}
	}
	
	private static boolean provjeriKonfig() {
		
		if(konfig.dajPostavku("port") == null) {
			return false;
		}
		if(konfig.dajPostavku("maks.cekaca") == null) {
			return false;
		}
		if(konfig.dajPostavku("server.aerodroma.port") == null) {
			return false;
		}
		if(konfig.dajPostavku("server.aerodroma.adresa") == null) {
			return false;
		}
		if(konfig.dajPostavku("maks.cekanje") == null) {
			return false;
		}
		if(konfig.dajPostavku("maks.dretve") == null) {
			return false;
		}
		return true;
	}
	
	private boolean ispitajPort(int port) {
		
		try {
			ServerSocket s = new ServerSocket(port);
			s.close();
		} catch (IOException e) {
			System.out.println("ERROR 14 INTERNAL >> Traženi port je vec zauzet!");
			return true;
		}
		return false;
	}

	public void obradaZahtjeva() {
		
		try {
			ss = new ServerSocket(this.port, this.maksCekaca);
			while (serverRadi) {
				if (dretve.size() < maksDretve) {
					DretvaUdaljenosti dretvaObrade = new DretvaUdaljenosti(ss.accept());
					dretvaObrade.start();
					dretve.add(dretvaObrade);
				}
			}
			ss.close();
		} catch (IOException ex) {
			System.out.println("INFO INTERNAL APK1 >> Server je zaustavljen!");
			return;
		}
	}
	
	private class DretvaUdaljenosti extends Thread {
		
		private Socket veza = null;
		
		public DretvaUdaljenosti(Socket veza) {
			super();
			this.veza = veza;
		}
		
		@Override
		public void run() {
			
			boolean vracenOdgovor = false;
			
			try (InputStreamReader isr = new InputStreamReader(this.veza.getInputStream(),
					Charset.forName("UTF-8"));
					OutputStreamWriter osw = new OutputStreamWriter(this.veza.getOutputStream(),
					Charset.forName("UTF-8"));) 
			{
				StringBuilder tekst = new StringBuilder();
				while (true) {
					int i = isr.read();
					if (i == -1) {
						break;
					}
					tekst.append((char) i);
				}
				this.veza.shutdownInput();
				
				Pattern pStatus = Pattern.compile(status);
				Pattern pQuit = Pattern.compile(quit);
				Matcher mStatus = pStatus.matcher(tekst.toString());
				Matcher mQuit = pQuit.matcher(tekst.toString());
				
			    if (mStatus.matches()) {
					izvrsiStatus(osw);
					vracenOdgovor = true;
			    }
			    else if (mQuit.matches()) {
					izvrsiQuit(osw);
					vracenOdgovor = true;
				}
			    
			    if (vracenOdgovor) {
			    	dretve.remove(dretve.size()-1);
			    	return;
			    }
			    
				Pattern pInit = Pattern.compile(init);
				Pattern pLoad = Pattern.compile(load);
				Pattern pDistanceIcaoIcao = Pattern.compile(distanceIcaoIcao);
				Pattern pClear = Pattern.compile(clear);
				
				Matcher mInit = pInit.matcher(tekst.toString());
				Matcher mLoad = pLoad.matcher(tekst.toString());
				Matcher mDistanceIcaoIcao = pDistanceIcaoIcao.matcher(tekst.toString());
				Matcher mClear = pClear.matcher(tekst.toString());
				
			    switch (statusServera) {
			    	
			    	case 0 : {
			    		if (mInit.matches()) {
			    			izvrsiInit(osw);
			    		}
			    		else if (mLoad.matches() || mDistanceIcaoIcao.matches() || mClear.matches()) {
			    			izvrsiError(osw, tekst.toString(), 1);
			    		}
			    		else {
			    			izvrsiError(osw, 14);
			    		} break;
			    	}
			    	case 1 : {
			    		if (mLoad.matches()) {
			    			izvrsiLoad(osw, tekst.toString());
			    		}
			    		else if (mInit.matches() || mDistanceIcaoIcao.matches() || mClear.matches()) {	//TODO zaustavit dretvu svuda
			    			izvrsiError(osw, tekst.toString(), 2);
			    		}
			    		else {
			    			izvrsiError(osw, 14);
			    		} break;
			    	}
			    	case 2 : {
			    		if(mDistanceIcaoIcao.matches()) {
							izvrsiDistanceIcaoIcao(osw, tekst.toString());
						}
			    		else if (mClear.matches()) {
							izvrsiClear(osw, tekst.toString());
						}
			    		else if (mInit.matches() || mLoad.matches()) {
							izvrsiError(osw, tekst.toString(), 3);
						}
			    		else {
			    			izvrsiError(osw, 14);
			    		} break;
			    	}
			    }
			    
			} catch (IOException e) {
				System.out.println("ERROR 14 INTERNAL >> Problem u stvaranju čitača i pisača za komunikaciju!");
			}
			dretve.remove(dretve.size()-1);
		}
	}
	
	public void izvrsiStatus(OutputStreamWriter osw) {
		String odgovor = "OK " + statusServera;
		ispisNaOSW(osw, odgovor);
	}

	private void izvrsiQuit(OutputStreamWriter osw) {
		String odgovor = "OK";
		ispisNaOSW(osw, odgovor);
		serverRadi = false;
		try {
			ss.close();
		} catch (IOException e) {
			System.out.println("ERROR 14 INTERNAL >> Problem sa zatvaranjem socketa!");
		}
	}

	public void izvrsiInit(OutputStreamWriter osw) {
		statusServera = 1;
		String odgovor = "OK";
		ispisNaOSW(osw, odgovor);		
	}
	
	public void izvrsiLoad(OutputStreamWriter osw, String tekst) {
		statusServera = 2;
		tekst = tekst.substring(5, tekst.length());
		try {
			aerodromi = ucitajAerodrome(tekst);
			int brojAerodroma = aerodromi.size();
			String odgovor = "OK " + brojAerodroma;
			ispisNaOSW(osw, odgovor);
		} catch (Exception e) {
			izvrsiError(osw, 14);
		}
	}
	
	public List<Aerodrom> ucitajAerodrome(String tekst) {
		
		Gson gson = new Gson();
		Type tipListaAerodroma = new TypeToken<ArrayList<Aerodrom>>(){}.getType();
		aerodromi = gson.fromJson(tekst, tipListaAerodroma);
		return aerodromi;
	}
	
	public void izvrsiError(OutputStreamWriter osw, String komanda, int kodGreske) {
		
		String odgovor = "";
		
		switch (kodGreske) {
			 case 1: {
				 odgovor = "ERROR 01 >> Server je u statusu hibernacije i ne može izvršiti >> '" + komanda + "'!";
			 } break;
			 case 2: {
				 odgovor = "ERROR 02 >> Server je u statusu inicijalizacije i ne može izvršiti >> '" + komanda + "'!";
			 } break;
			 case 3: {
				 odgovor = "ERROR 03 >> Server je u statusu aktivan i ne može izvršiti >> '" + komanda + "'!";
			 } break;
			 default :{
				 odgovor = "ERROR 14 >> Nepoznata komanda ili nedifinirana greška!";
			 }
		}
		ispisNaOSW(osw, odgovor);
	}
	
	public void izvrsiError(OutputStreamWriter osw, int kodGreske) {
		
		String odgovor = "";
		
		switch (kodGreske) {
			 case 11: {
				 odgovor = "ERROR 11 >> Nema prvog traženog aerodroma!";
			 } break;
			 case 12: {
				 odgovor = "ERROR 12 >> Nema drugog traženog aerodroma!";
			 } break;
			 case 13: {
				 odgovor = "ERROR 13 >> Nema oba tražena aerodroma!";
			 } break;
			 case 14: {
				 odgovor = "ERROR 14 >> Nepoznata komanda ili nedifinirana greška!";
			 } break;
			 default :{
				 odgovor = "ERROR 14 >> Nepoznata komanda ili nedifinirana greška!";
			 }
		}
		ispisNaOSW(osw, odgovor);
	}

	private void izvrsiDistanceIcaoIcao(OutputStreamWriter osw, String komanda) {
		
		String[] p = komanda.split(" ");
		String icao = p[1];
		String icao2 = p[2];
		
		Aerodrom a1 = null;
		Aerodrom a2 = null;
		String udaljenost = null;
		String odgovor = null;
		
		for(Aerodrom a : aerodromi) {
			if(a.getIcao().compareTo(icao) == 0) {
				a1 = a;
				break;
			}
		}
		for(Aerodrom a : aerodromi) {
			if(a.getIcao().compareTo(icao2) == 0) {
				a2 = a;
				break;
			}
		}
		if (a1 == null && a2 == null) {
			izvrsiError(osw, 13);
			return;
		}
		else if(a1 == null) {
			izvrsiError(osw, 11);
			return;
		}
		else if(a2 == null) {
			izvrsiError(osw, 12);
			return;
		}
		udaljenost = izracunajUdaljenost(a1, a2);
		odgovor = "OK " + udaljenost;
		
		ispisNaOSW(osw, odgovor);
	}

	private String izracunajUdaljenost(Aerodrom a1, Aerodrom a2) {
		
		double a1GD = Double.parseDouble(a1.getLokacija().getLongitude());
		double a1GS = Double.parseDouble(a1.getLokacija().getLatitude());
		double a2GD = Double.parseDouble(a1.getLokacija().getLongitude());
		double a2GS = Double.parseDouble(a2.getLokacija().getLatitude());
		
		double udaljenostD = Math.toRadians(a2GD - a1GD);
		double udaljenostS = Math.toRadians(a2GS - a1GS);
		
		double izracun = Math.pow(Math.sin(udaljenostS / 2), 2)
				+ Math.cos(Math.toRadians(a1GS)) * Math.cos(Math.toRadians(a2GS))
				* Math.pow(Math.sin(udaljenostD / 2), 2);
		
		double udaljenost = (2 * Math.atan2(Math.sqrt(izracun), Math.sqrt(1 - izracun))) * 6371;
		return df.format(udaljenost);
	}

	private void izvrsiClear(OutputStreamWriter osw, String komanda) {
		statusServera = 0;
		aerodromi.clear();
		ispisNaOSW(osw, "OK");
		return;
	}
	
	private void ispisNaOSW(OutputStreamWriter osw, String odgovor) {
		try {
			osw.write(odgovor);
			osw.flush();
			osw.close();
		} catch (IOException e) {
			System.out.println("ERROR 14 INTERNAL >> Problem sa slanjem odgovora!");
		}
	}
}
