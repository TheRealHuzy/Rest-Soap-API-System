package org.foi.nwtis.ihuzjak.aplikacija_3.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.Korisnik;
import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.Token;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

public class ProvjeraDAO {
	
	Konfiguracija konfig;
	Connection con;
	String administrator;
	long zetonTrajanje;
	
	@Inject
	ServletContext context;
	
	String url;
	PreparedStatement pstmt;
	
	public ProvjeraDAO(Konfiguracija konfig){
		this.konfig = konfig;
		url = konfig.dajPostavku("server.database")+konfig.dajPostavku("user.database");
		administrator = konfig.dajPostavku("sustav.administratori");
		zetonTrajanje = Long.parseLong(konfig.dajPostavku("zeton.trajanje"));
	}
	

	public Token dajAutentifikacijuNoviToken(String korisnik, String lozinka) {
		
		String upit = "SELECT * FROM KORISNICI WHERE KORISNIK = '" + korisnik + "' AND LOZINKA = '" + lozinka + "';";
		Token token = null;
		List<Korisnik> korisnici = saljiNaBazuKorisnik(upit);
		if (korisnici.isEmpty()) {
			return token;
		}
		
		long trenutnoVrijeme = System.currentTimeMillis();
		upit = "INSERT INTO TOKENI (ID, KORISNIK, VRIJEDIDO) VALUES (DEFAULT, '" + korisnik + "', "
				+ (trenutnoVrijeme + zetonTrajanje) + ");";
		saljiNaBazuUnosTokena(upit);
		
		upit = "SELECT * FROM TOKENI WHERE KORISNIK = '" + korisnik + "';";
		token = saljiNaBazuToken(upit);
		return token;
	}

	public boolean dajAutentifikaciju(String korisnik, String lozinka) {
		
		String upit = "SELECT * FROM KORISNICI WHERE KORISNIK = '" + korisnik + "' AND LOZINKA = '" + lozinka + "';";
		List<Korisnik> korisnici = saljiNaBazuKorisnik(upit);
		if (!korisnici.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public boolean dajAdminaInner(String korisnik) {
		
		String upit = "SELECT * FROM ULOGE WHERE KORISNIK = '" + korisnik + "' AND GRUPA = '" + administrator + "';";
		return saljiNaBazuBoolean(upit);
	}
	
	public int dajTokenProvjeri(String korisnik, String token) {
		
		String upit = "SELECT * FROM TOKENI WHERE KORISNIK = '" + korisnik + "' AND ID = " + token + ";";
		int odgovor = 0;
		boolean jestKorisnikovToken = saljiNaBazuBoolean(upit);
		if (!jestKorisnikovToken) {
			return odgovor;
		}
		
		long trenutnoVrijeme = System.currentTimeMillis();
		upit = "SELECT * FROM TOKENI WHERE KORISNIK = '" + korisnik + "' AND ID = " + token
				+ " AND VRIJEDIDO >= " + trenutnoVrijeme + " ;";
		boolean nijeIstekao = saljiNaBazuBoolean(upit);
		if (!nijeIstekao) {
			odgovor = 1;
		}
		else {
			odgovor = 2;
		}
		return odgovor;
	}
	
	public int dajTokenIzbrisi(String korisnik, String token) {
		
		String upit = "SELECT * FROM TOKENI WHERE KORISNIK = '" + korisnik + "' AND ID = " + token + ";";
		int odgovor = 0;
		boolean jestKorisnikovToken = saljiNaBazuBoolean(upit);
		if (!jestKorisnikovToken) {
			return odgovor;
		}
		
		long trenutnoVrijeme = System.currentTimeMillis();
		upit = "SELECT * FROM TOKENI WHERE KORISNIK = '" + korisnik + "' AND ID = " + token
				+ " AND VRIJEDIDO >= " + trenutnoVrijeme + " ;";
		boolean nijeIstekao = saljiNaBazuBoolean(upit);
		if (!nijeIstekao) {
			odgovor = 1;
		}
		else {
			odgovor = 2;
			upit = "UPDATE TOKENI SET VRIJEDIDO = 0 WHERE KORISNIK = '" + korisnik + "' AND ID = " + token + " ;";
			saljiNaBazuVoid(upit);
		}
		return odgovor;
	}
	
	public int dajAutorizacijuZaBrisanjeSvihTokena(String korisnik, String korisnikZaBrisanje) {
		
		String upit = "SELECT * FROM ULOGE WHERE KORISNIK = '" + korisnik + "' AND GRUPA = '" + administrator + "';";
		int odgovor = 0;
		boolean autorizacija = saljiNaBazuBoolean(upit);
		if (!autorizacija) {
			return odgovor;
		}
		
		long trenutnoVrijeme = System.currentTimeMillis();
		upit = "SELECT * FROM TOKENI WHERE KORISNIK = '" + korisnikZaBrisanje
				+ "' AND VRIJEDIDO >= " + trenutnoVrijeme + ";";		
		boolean imaAktivnihTokena = saljiNaBazuBoolean(upit);
		if (!imaAktivnihTokena) {
			odgovor = 1;
		}
		else {
			odgovor = 2;
			upit = "UPDATE TOKENI SET VRIJEDIDO = 0 WHERE KORISNIK = '" + korisnikZaBrisanje + "';";
			saljiNaBazuVoid(upit);
		}		
		return odgovor;
	}

	private List<Korisnik> saljiNaBazuKorisnik(String upit) {
		List<Korisnik> korisnici = new ArrayList<>();
		try {
			 Class.forName(getDriverDatabase(url, konfig));
		} catch (Exception e) {
		     e.printStackTrace();
		}
        try
        {   con = DriverManager.getConnection(url, konfig.dajPostavku("user.username"),
        		konfig.dajPostavku("user.password"));
        
            pstmt = con.prepareStatement(upit);
            ResultSet rs = pstmt.executeQuery();
                
            while (rs.next()) {
                Korisnik k = new Korisnik("","","","","");
                k.setKorisnik(rs.getString("KORISNIK"));
                k.setLozinka(rs.getString("LOZINKA"));
                k.setPrezime(rs.getString("PREZIME"));
                k.setIme(rs.getString("IME"));
                k.setEmail(rs.getString("EMAIL"));
                korisnici.add(k);
            }
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            Korisnik k = new Korisnik("","","","","");
            k.setKorisnik("SQLException");
            korisnici.add(k);
        }
        return korisnici;
	}
	
	private boolean saljiNaBazuUnosTokena(String upit) {
		try {
			 Class.forName(getDriverDatabase(url, konfig));
		} catch (Exception e) {
		     e.printStackTrace();
		}
		boolean odgovor = false;
        try
        {   con = DriverManager.getConnection(url, konfig.dajPostavku("user.username"),
        		konfig.dajPostavku("user.password"));
        
            pstmt = con.prepareStatement(upit);
            pstmt.execute();
            odgovor = true;
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return odgovor;
	}
	
	private boolean saljiNaBazuBoolean(String upit) {
		try {
			 Class.forName(getDriverDatabase(url, konfig));
		} catch (Exception e) {
		     e.printStackTrace();
		}
		boolean odgovor = false;
        try
        {   con = DriverManager.getConnection(url, konfig.dajPostavku("user.username"),
        		konfig.dajPostavku("user.password"));

            pstmt = con.prepareStatement(upit);
            ResultSet rs = pstmt.executeQuery();    
            while (rs.next()) {
                odgovor = true;
            }
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return odgovor;
	}
	
	private void saljiNaBazuVoid(String upit) {
		try {
			 Class.forName(getDriverDatabase(url, konfig));
		} catch (Exception e) {
		     e.printStackTrace();
		}
        try
        {   con = DriverManager.getConnection(url, konfig.dajPostavku("user.username"),
        		konfig.dajPostavku("user.password"));
        
            pstmt = con.prepareStatement(upit);
            pstmt.execute();
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
	}
	
	private Token saljiNaBazuToken(String upit) {
		Token token = null;
		try {
			 Class.forName(getDriverDatabase(url, konfig));
		} catch (Exception e) {
		     e.printStackTrace();
		}
        try
        {   con = DriverManager.getConnection(url, konfig.dajPostavku("user.username"),
        		konfig.dajPostavku("user.password"));
        
            pstmt = con.prepareStatement(upit);
            ResultSet rs = pstmt.executeQuery();
            token = new Token("", "", "");
            while (rs.next()) {
                token.setId(rs.getString("ID"));
                token.setKorisnik(rs.getString("KORISNIK"));
                token.setVrijediDo(rs.getString("VRIJEDIDO"));
            }
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return token;
	}
	
    public String getDriverDatabase(String urlBazePodataka, Konfiguracija konfig) {
    	String protokol = null;
    	String s[] = urlBazePodataka.split("//");
    	protokol = s[0].substring(0, s[0].length() - 1);
    	protokol = protokol.replace(":", ".");
    	String driver = konfig.dajPostavku(protokol);
    	return driver;
    }
}
