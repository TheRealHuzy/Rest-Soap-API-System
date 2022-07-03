package org.foi.nwtis.ihuzjak.aplikacija_3.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.rest.podaci.AvionLeti;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

public class AerodromiDolasciDAO {
	
	@Inject
	ServletContext context;
	
	String url;
	Connection con;
	PreparedStatement pstmt;
	
	Konfiguracija konfig;
	
	public AerodromiDolasciDAO(Konfiguracija konfig){
		this.konfig = konfig;
		url = konfig.dajPostavku("server.database")+konfig.dajPostavku("user.database");
	}
	
	public List<AvionLeti> dajAerodromeDolaske(String icao, String vrijemeOd, String vrijemeDo, int vrsta){
		
		if (vrsta == 0) {
			vrijemeOd = pretvoriDatumULong(vrijemeOd);
			vrijemeDo = pretvoriDatumULong(vrijemeDo);
		}
		
		String upit = "SELECT * FROM AERODROMI_DOLASCI WHERE ESTDEPARTUREAIRPORT='" + icao + "'"
				+ " AND LASTSEEN >=" + vrijemeOd
				+ " AND LASTSEEN <=" + vrijemeDo
				+ ";";
		
		List<AvionLeti> aerodromi = saljiNaBazu(upit);
        return aerodromi;
	}
	
	private String pretvoriDatumULong(String vrijeme) {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		long milis = 0;
		try {
		    Date date = sdf.parse(vrijeme);
		    milis = date.getTime();
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		return Long.toString(milis/1000);
	}

	private List<AvionLeti> saljiNaBazu(String upit) {
		List<AvionLeti> aerodromi = new ArrayList<>();
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
            	AvionLeti a = new AvionLeti();
                a.setIcao24(rs.getString("icao24"));
                
                a.setArrivalAirportCandidatesCount(rs.getInt("ArrivalAirportCandidatesCount"));
                a.setCallsign(rs.getString("callsign"));
                a.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                a.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                a.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                a.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                a.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                a.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                a.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                a.setFirstSeen(rs.getInt("firstSeen"));
                a.setLastSeen(rs.getInt("lastSeen"));
                aerodromi.add(a);
            }
            pstmt.close();
            con.close();
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            AvionLeti a = new AvionLeti();
            a.setIcao24("SQLException: " + ex.getMessage());
            aerodromi.add(a);
            ex.printStackTrace();
        }
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
