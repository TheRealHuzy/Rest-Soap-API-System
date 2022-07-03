package org.foi.nwtis.ihuzjak.aplikacija_5.RAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

import com.google.gson.Gson;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class AerodromRAO {
	
	public List<AvionLeti> dajPolaskeDan(String korisnik, String token, String icao, String danOd, String danDo) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi/"
				+ icao + "/polasci?vrsta=0&od=" + danOd + "&do=" + danDo);
		Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("token", token)
				.get();
		List<AvionLeti> letovi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			letovi = new ArrayList<>();
			letovi.addAll(Arrays.asList(gson.fromJson(odgovor, AvionLeti[].class)));
		}
		return letovi;
	}
	
	public List<AvionLeti> dajPolaskeVrijeme(String korisnik, String token, String icao, String danOd, String danDo) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi/"
				+ icao + "/polasci?vrsta=1&od=" + danOd + "&do=" + danDo);
		Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("token", token)
				.get();
		List<AvionLeti> letovi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			letovi = new ArrayList<>();
			letovi.addAll(Arrays.asList(gson.fromJson(odgovor, AvionLeti[].class)));
		}
		return letovi;
	}
	
	public boolean dodajAerodromPreuzimanje(String korisnik, String token, String icao) {
        
		Client client = ClientBuilder.newClient();
        WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi");
        Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
            .header("icao", icao)
        	.header("korisnik", korisnik)
        	.header("token", token)
        	.post(Entity.json(korisnik), Response.class);
        boolean dodavanje = false;
		if (restOdgovor.getStatus() == 200) {
			dodavanje = true;
		}
		return dodavanje;
    }
	
	public Aerodrom dajAerodrom(String icao, String korisnik, String token) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi").path(icao);
		Response restOdgovor = webResource.request(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("token", token)
				.get();
		Aerodrom aerodrom = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			List<Aerodrom> aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
			aerodrom = aerodromi.get(0);
		}
		return aerodrom;
	}
	
	public String dajBrojPracenihAerodroma() {
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi/praceni/broj");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		String odgovor = null;
		if (restOdgovor.getStatus() == 200) {
			odgovor = restOdgovor.readEntity(String.class);
		}
		return odgovor;
	}
}
