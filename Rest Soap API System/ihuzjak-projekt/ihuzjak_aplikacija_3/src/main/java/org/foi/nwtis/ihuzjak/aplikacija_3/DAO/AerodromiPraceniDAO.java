package org.foi.nwtis.ihuzjak.aplikacija_3.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.AerodromPracen;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

public class AerodromiPraceniDAO {
	
	@Inject
	ServletContext context;
	
	String url;
	Connection con;
	PreparedStatement pstmt;
	
	Konfiguracija konfig;
	
	public AerodromiPraceniDAO(Konfiguracija konfig){
		this.konfig = konfig;
		url = konfig.dajPostavku("server.database")+konfig.dajPostavku("user.database");
	}
	
	public List<AerodromPracen> dajSvePraceneAerodrome(){
		
		String upit = "SELECT * FROM AERODROMI_PRACENI;";
		List<AerodromPracen> aerodromi = saljiNaBazu(upit);
        return aerodromi;
	}
	
	public int dajBrojPracenihAerodroma(){
		
		String upit = "SELECT COUNT(*) FROM AERODROMI_PRACENI;";
		return dajBrojZapisa(upit);
	}

	private List<AerodromPracen> saljiNaBazu(String upit) {
		List<AerodromPracen> aerodromi = new ArrayList<>();
		try {
			 Class.forName(getDriverDatabase(url, konfig));
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
        try
        {   
        	con = DriverManager.getConnection(url, konfig.dajPostavku("user.username"),
        			konfig.dajPostavku("user.password"));
            pstmt = con.prepareStatement(upit);
            ResultSet rs = pstmt.executeQuery();
                
            while (rs.next()) {
            	AerodromPracen a = new AerodromPracen("","");
                a.setIdent(rs.getString("ident"));
                aerodromi.add(a);
            }
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            AerodromPracen a = new AerodromPracen("","");
            a.setIdent("SQLException: " + ex.getMessage());
            aerodromi.add(a);
            ex.printStackTrace();
        }
        return aerodromi;
	}
	
	public int dajBrojZapisa(String upit) {

		int broj = 1;
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
        	   broj = rs.getInt(1);
           }
           pstmt.close();
           con.close();
       } catch(SQLException ex) {
           System.err.println("SQLException: " + ex.getMessage());
           ex.printStackTrace();
       }
		return broj;
	}
	
	public boolean posaljiAerodrom(String icao){

		String upit = "INSERT INTO AERODROMI_PRACENI (ident, stored) VALUES ('" + icao +"', CURRENT_TIMESTAMP);";
		boolean uspjesanUpit = upisiNaBazu(upit);
		return uspjesanUpit;
	}

	private boolean upisiNaBazu(String upit) {
		try {
			 Class.forName(getDriverDatabase(url, konfig));
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		boolean uspjesanUpit = false;
        try
        {   
        	con = DriverManager.getConnection(url, konfig.dajPostavku("user.username"),
        			konfig.dajPostavku("user.password"));
            pstmt = con.prepareStatement(upit);
            pstmt.execute();
            uspjesanUpit = true;
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
            try {
				if (!pstmt.isClosed()) {
		            pstmt.close();
				}
				if (!con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        return uspjesanUpit;
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