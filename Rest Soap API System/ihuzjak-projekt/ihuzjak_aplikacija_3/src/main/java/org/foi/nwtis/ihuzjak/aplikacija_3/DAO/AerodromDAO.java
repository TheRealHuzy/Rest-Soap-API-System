package org.foi.nwtis.ihuzjak.aplikacija_3.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.podaci.Airport;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

public class AerodromDAO {
	
	@Inject
	ServletContext context;
	
	String url;
	Connection con;
	PreparedStatement pstmt;
	
	Konfiguracija konfig;
	
	public AerodromDAO(Konfiguracija konfig){
		this.konfig = konfig;
		url = konfig.dajPostavku("server.database")+konfig.dajPostavku("user.database");
	}
	
	public List<Airport> dajSveAerodrome(){
		
		String upit = "SELECT * FROM AIRPORTS;";
		List<Airport> aerodromi = saljiNaBazu(upit);
        return aerodromi;
	}

	private List<Airport> saljiNaBazu(String upit) {
		List<Airport> aerodromi = new ArrayList<>();
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
                Airport a = new Airport();
                a.setIdent(rs.getString("ident"));
                a.setName(rs.getString("name"));
                a.setIso_country(rs.getString("iso_country"));
                a.setCoordinates(rs.getString("coordinates"));
                aerodromi.add(a);
            }
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            Airport a = new Airport();
            a.setIdent("SQLException: " + ex.getMessage());
            aerodromi.add(a);
            ex.printStackTrace();
        }
        return aerodromi;
	}
	
	public List<Airport> dajAerodrom(String icao){
		String upit = "SELECT * FROM AIRPORTS WHERE ident='" + icao + "';";
		List<Airport> aerodromi = saljiNaBazu(upit);
        return aerodromi;
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
