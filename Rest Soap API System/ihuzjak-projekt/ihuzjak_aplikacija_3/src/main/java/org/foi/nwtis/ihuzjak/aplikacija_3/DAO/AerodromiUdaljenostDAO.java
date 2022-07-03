package org.foi.nwtis.ihuzjak.aplikacija_3.DAO;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.Connection;

import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;

public class AerodromiUdaljenostDAO {
	
	Konfiguracija konfig;
	int port;
	String adresa;
	Connection con;
	
	public AerodromiUdaljenostDAO(Konfiguracija konfig){
		this.konfig = konfig;
		port = Integer.parseInt(konfig.dajPostavku("server.udaljenosti.port"));
		adresa = konfig.dajPostavku("server.udaljenosti.adresa");
	}
	
	public int getPort() {
		return port;
	}
	
	public String getAdresa() {
		return adresa;
	}
	
	public String dajUdaljenost(String icao1, String icao2) {
		
		String komanda = "DISTANCE " + icao1 + " " + icao2;
		String odgovor = saljiNaServer(komanda);
		if (odgovor.startsWith("OK")) {
			odgovor = "udaljenost: " + odgovor.substring(3);
		}
		return odgovor;
	}
	
	public String dajStatus() {
		
		String komanda = "STATUS";
		String odgovor = saljiNaServer(komanda);
		return odgovor;
	}
	
	public String posaljiKomandu(String komanda) {
		
		String odgovor = "";
		if (komanda.compareTo("QUIT") == 0 || komanda.compareTo("INIT") == 0 || komanda.compareTo("CLEAR") == 0) {
			odgovor = saljiNaServer(komanda);
		}
		else {
			odgovor = "INFO INTERNAL APK3 >> Korištenje nedozvoljene ili nepostojeće komande!";
		}
		
		return odgovor;
	}
	

	public String posaljiLoadInner(String aerodromi) {

		String komanda = "LOAD " + aerodromi;
		String odgovor = saljiNaServer(komanda);
		if (odgovor.startsWith("OK")) {
			odgovor = odgovor.substring(3);
		}
		return odgovor;
	}

	private String saljiNaServer(String komanda) {
		try (Socket veza = new Socket(adresa, port);
				InputStreamReader isr = new InputStreamReader(veza.getInputStream(), Charset.forName("UTF-8"));
				OutputStreamWriter osw = new OutputStreamWriter(veza.getOutputStream(), Charset.forName("UTF-8"));)
		{
			osw.write(komanda);
			osw.flush();
			veza.shutdownOutput();
			StringBuilder tekst = new StringBuilder();
			while (true) {
				int i = isr.read();
				if (i == -1) {
					break;
				}
				tekst.append((char) i);
			}
			veza.shutdownInput();
			veza.close();
			return tekst.toString();
		} catch (Exception e) {
			System.out.println("INFO INTERNAL APK3 >> Neuspješno stvaranje socketa ili druga nedefinirana greška!");
		}
		return "";
	}
}
