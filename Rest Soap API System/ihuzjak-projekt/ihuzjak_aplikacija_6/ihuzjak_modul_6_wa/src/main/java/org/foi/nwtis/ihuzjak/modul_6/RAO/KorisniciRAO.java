package org.foi.nwtis.ihuzjak.modul_6.RAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.ihuzjak.modul_6.podaci.TokenV;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.ihuzjak.modul_6.podaci.Korisnik;

import com.google.gson.Gson;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class KorisniciRAO {
	
	@Inject
	ServletContext context;
	
	String url;
	Connection con;
	PreparedStatement pstmt;
	
	Konfiguracija konfig;
	
	public KorisniciRAO(Konfiguracija konfig){
		
		this.konfig = konfig;
		url = konfig.dajPostavku("server.database") + konfig.dajPostavku("user.database");
	}
	
	public List<Korisnik> dajKorisnike(String korisnik, String token) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/korisnici");
		Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("token", token)
				.get();
		List<Korisnik> korisnici = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			korisnici = new ArrayList<>();
			korisnici.addAll(Arrays.asList(gson.fromJson(odgovor, Korisnik[].class)));
		}
		return korisnici;
	}
	
	public String dajToken(String korisnik, String lozinka) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/provjere");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("lozinka", lozinka)
				.get();
		String idTokena = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			TokenV token = gson.fromJson(odgovor, TokenV.class);
			idTokena = token.getToken();
		}
		return idTokena;
    }
	
	public void brisiMojToken(String token, String korisnik, String lozinka) {
        
		Client client = ClientBuilder.newClient();
        WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/provjere/" + token);
        webResource.request(MediaType.APPLICATION_JSON)
        	.header("korisnik", korisnik)
        	.header("lozinka", lozinka)
        	.delete(String.class);
    }
}
