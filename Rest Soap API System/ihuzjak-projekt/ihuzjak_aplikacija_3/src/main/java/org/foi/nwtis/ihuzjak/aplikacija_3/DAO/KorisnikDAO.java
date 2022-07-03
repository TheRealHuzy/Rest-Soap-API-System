package org.foi.nwtis.ihuzjak.aplikacija_3.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.Grupa;
import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.Korisnik;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;

/**
 *
 * @author NWTiS
 * ! changed to work with class 'Konfiguracija' instead of 'PostavkeBazaPodataka' !
 * ! added method to work with class 'Grupa' !
 * ! changed dohvati korisnika !
 */

public class KorisnikDAO {
	
	Konfiguracija konfig;

	public KorisnikDAO(Konfiguracija konfig){
		this.konfig = konfig;
	}

    public Korisnik dohvatiKorisnika(String korisnik, Konfiguracija konfig) {
        String url = konfig.dajPostavku("server.database") + konfig.dajPostavku("user.database");
        String bpkorisnik = konfig.dajPostavku("user.username");
        String bplozinka = konfig.dajPostavku("user.password");
        String upit = "SELECT ime, prezime, korisnik, lozinka, email FROM korisnici WHERE korisnik = '"
        		+ korisnik + "';";

        try {
            Class.forName(getDriverDatabase(url, konfig));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("email");

                    Korisnik k = new Korisnik(korisnik1, "******", prezime, ime, email);
                    return k;
                }

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Korisnik> dohvatiSveKorisnike(Konfiguracija konfig) {
        String url = konfig.dajPostavku("server.database") + konfig.dajPostavku("user.database");
        String bpkorisnik = konfig.dajPostavku("user.username");
        String bplozinka = konfig.dajPostavku("user.password");
        String upit = "SELECT ime, prezime, email, korisnik, lozinka FROM korisnici";

        try {
            Class.forName(getDriverDatabase(url, konfig));

            List<Korisnik> korisnici = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     Statement s = con.createStatement();
                     ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("email");
                    Korisnik k = new Korisnik(korisnik1, "******", prezime, ime, email);

                    korisnici.add(k);
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean azurirajKorisnika(Korisnik k, String lozinka, Konfiguracija konfig) {
        String url = konfig.dajPostavku("server.database") + konfig.dajPostavku("user.database");
        String bpkorisnik = konfig.dajPostavku("user.username");
        String bplozinka = konfig.dajPostavku("user.password");
        String upit = "UPDATE korisnici SET ime = ?, prezime = ?, email = ?, lozinka = ? "
                + " WHERE korisnik = ?";

        try {
            Class.forName(getDriverDatabase(url, konfig));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, k.ime);
                s.setString(2, k.prezime);
                s.setString(3, k.email);
                s.setString(4, lozinka);
                s.setString(5, k.korisnik);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean dodajKorisnika(Korisnik k, Konfiguracija konfig) {
        String url = konfig.dajPostavku("server.database") + konfig.dajPostavku("user.database");
        String bpkorisnik = konfig.dajPostavku("user.username");
        String bplozinka = konfig.dajPostavku("user.password");
        String upit = "INSERT INTO korisnici (ime, prezime, email, korisnik, lozinka) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            Class.forName(getDriverDatabase(url, konfig));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, k.ime);
                s.setString(2, k.prezime);
                s.setString(3, k.email);
                s.setString(4, k.korisnik);
                s.setString(5, k.lozinka);
                
                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (Exception ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public String getDriverDatabase(String urlBazePodataka, Konfiguracija konfig) {
    	String protokol = null;
    	String s[] = urlBazePodataka.split("//");
    	protokol = s[0].substring(0, s[0].length() - 1);
    	protokol = protokol.replace(":", ".");
    	String driver = konfig.dajPostavku(protokol);
    	return driver;
    }

	public List<Grupa> dohvatiGrupe(String korisnik, Konfiguracija konfig) {
		String url = konfig.dajPostavku("server.database") + konfig.dajPostavku("user.database");
        String bpkorisnik = konfig.dajPostavku("user.username");
        String bplozinka = konfig.dajPostavku("user.password");
        String upit = "SELECT g.GRUPA,g.NAZIV FROM GRUPE g INNER JOIN ULOGE u ON g.GRUPA = u.GRUPA WHERE u.KORISNIK = '"
        		+ korisnik + "';";
        try {
            Class.forName(getDriverDatabase(url, konfig));

            List<Grupa> grupe = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     Statement s = con.createStatement();
                     ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String grupa = rs.getString("GRUPA");
                    String naziv = rs.getString("NAZIV");
                    Grupa g = new Grupa(grupa, naziv);

                    grupe.add(g);
                }
                return grupe;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		return null;
	}

}
