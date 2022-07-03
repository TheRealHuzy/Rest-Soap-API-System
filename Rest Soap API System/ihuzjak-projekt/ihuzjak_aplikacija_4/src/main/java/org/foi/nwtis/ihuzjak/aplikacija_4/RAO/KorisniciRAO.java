package org.foi.nwtis.ihuzjak.aplikacija_4.RAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.ihuzjak.aplikacija_4.podaci.Korisnik;
import org.foi.nwtis.ihuzjak.aplikacija_4.podaci.TokenV;

import com.google.gson.Gson;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class KorisniciRAO {
	
	public List<Korisnik> dajSveKorisnike(String korisnik, String token) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/korisnici");
		Response restOdgovor = webResource.request()
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
	
	public boolean jesamLiAdmin(String korisnik) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/provjere/admin");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.get();
		boolean odgovor = false;
		if (restOdgovor.getStatus() == 200) {
			odgovor = true;
		}
		return odgovor;
	}
	
	public void brisiTokeneKorisnika(String korisnikZaBrisanje, String korisnik, String lozinka) {
		
        Client client = ClientBuilder.newClient();
        WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/provjere/korisnik/" + korisnikZaBrisanje);
        webResource.request(MediaType.APPLICATION_JSON)
        	.header("korisnikHead", korisnik)
        	.header("lozinka", lozinka)
        	.delete(String.class);
    }
	
	public void brisiMojToken(String token, String korisnik, String lozinka) {
		
        Client client = ClientBuilder.newClient();
        WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/provjere/" + token);
        webResource.request(MediaType.APPLICATION_JSON)
        	.header("korisnik", korisnik)
        	.header("lozinka", lozinka)
        	.delete(String.class);
    }

	public void dodajKorisnika(String korisnik, String lozinka, String ime, String prezime, String email) {
        Client client = ClientBuilder.newClient();
        WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/korisnici");
        webResource.request(MediaType.APPLICATION_JSON)
        	.header("korisnik", korisnik)
        	.header("lozinka", lozinka)
        	.header("ime", ime)
        	.header("prezime", prezime)
        	.header("email", email)
            .post(Entity.json(korisnik), String.class);
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
}
