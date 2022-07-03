package org.foi.nwtis.ihuzjak.modul_6.RAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.ihuzjak.modul_6.podaci.AerodromPracen;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

import com.google.gson.Gson;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class AerodromiRAO {
	
	@Inject
	ServletContext context;
	
	String url;
	Connection con;
	PreparedStatement pstmt;
	
	Konfiguracija konfig;
	
	public AerodromiRAO(Konfiguracija konfig){
		
		this.konfig = konfig;
		url = konfig.dajPostavku("server.database") + konfig.dajPostavku("user.database");
	}
	
	public List<Aerodrom> dajAerodrome(String korisnik, String token) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi");
		Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("token", token)
				.get();
		List<Aerodrom> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
		}
		return aerodromi;
	}
	
	public List<AerodromPracen> dajPracene(String korisnik, String token) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi/praceni");
		Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.get();
		List<AerodromPracen> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, AerodromPracen[].class)));
		}
		return aerodromi;
	}
	
	public Aerodrom dajAerodrom(String icao, String korisnik, String token) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi/").path(icao);
		Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("token", token)
				.get();
		List<Aerodrom> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
		}
		return aerodromi.get(0);
	}
	
	public void dodajAerodrom(String icao, String korisnik, String token) {
        Client client = ClientBuilder.newClient();
        WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi");
        webResource.request(MediaType.APPLICATION_JSON)
        	.header("icao", icao)
        	.header("korisnik", korisnik)
        	.header("token", token)
            .post(Entity.json(korisnik), String.class);
    }
	
	public List<AvionLeti> dajDpolaskeDatumLong(String icao, String vrsta, String pd, String vrijemeOd, String vrijemeDo,
			String korisnik, String token) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi/").path(icao)
				.path(pd).queryParam("vrsta", vrsta).queryParam("od", vrijemeOd).queryParam("do", vrijemeDo);
		Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("token", token)
				.get();
		List<AvionLeti> avioni = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			avioni = new ArrayList<>();
			avioni.addAll(Arrays.asList(gson.fromJson(odgovor, AvionLeti[].class)));
		}
		return avioni;
	}
}
