package org.foi.nwtis.ihuzjak.aplikacija_3.rest;

import java.util.List;

import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.KorisnikDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.ProvjeraDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.Grupa;
import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.Korisnik;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("korisnici")
public class RestKorisnici {
	
	@Inject
    ServletContext context;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajSveKorisnike(@HeaderParam("korisnik") String korisnik, @HeaderParam("token") String token) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		ProvjeraDAO pDAO = new ProvjeraDAO(konfig);
		int tokenStatus = pDAO.dajTokenProvjeri(korisnik, token);
		switch (tokenStatus) {
			case 0 : {
				odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Traženi token ne postoji ili nije Vaš").build();
			} break;
			case 1 : {
				odgovor = Response.status(Response.Status.REQUEST_TIMEOUT).entity("Token je istekao.").build();
			} break;
			case 2 : {
				KorisnikDAO kDAO = new KorisnikDAO(konfig);
				List<Korisnik> korisnici = kDAO.dohvatiSveKorisnike(konfig);
				
				if(!korisnici.isEmpty()) {
					odgovor = Response.status(Response.Status.OK).entity(korisnici).build();
				} else {
					odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema korisnika.").build();
				}
			}
		}
		return odgovor;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response dodajKorisnika(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka,
    		@HeaderParam("ime") String ime, @HeaderParam("prezime") String prezime, @HeaderParam("email") String email) {
		
		Korisnik k =  new Korisnik(korisnik, lozinka, ime, prezime, email);
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		KorisnikDAO kDAO = new KorisnikDAO(konfig);
		boolean uspjesnoDodavanje = kDAO.dodajKorisnika(k, konfig);
		Response odgovor;
		
		if(uspjesnoDodavanje) {
			odgovor = Response.status(Response.Status.OK).entity("Korisnik je uspješno dodan.").build();
		} else {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Korisnik nije dodan.").build();
		}
		return odgovor;
	}
	
	@GET
	@Path("{korisnik}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajKorisnika(@PathParam("korisnik") String korisnik,
			@HeaderParam("korisnikHead") String korisnikHead, @HeaderParam("token") String token) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		ProvjeraDAO pDAO = new ProvjeraDAO(konfig);
		int tokenStatus = pDAO.dajTokenProvjeri(korisnikHead, token);
		switch (tokenStatus) {
			case 0 : {
				odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Traženi token ne postoji ili nije Vaš").build();
			} break;
			case 1 : {
				odgovor = Response.status(Response.Status.REQUEST_TIMEOUT).entity("Token je istekao.").build();
			} break;
			case 2 : {
				KorisnikDAO kDAO = new KorisnikDAO(konfig);
				Korisnik k = kDAO.dohvatiKorisnika(korisnik, konfig);
				
				if(k != null) {
					odgovor = Response.status(Response.Status.OK).entity(k).build();
				} else {
					odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podatka.").build();
				}
			}
		}
		return odgovor;
	}
	
	@GET
	@Path("{korisnik}/grupe")
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajGrupeKorisnika(@PathParam("korisnik") String korisnik,
			@HeaderParam("korisnikHead") String korisnikHead, @HeaderParam("token") String token) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		ProvjeraDAO pDAO = new ProvjeraDAO(konfig);
		int tokenStatus = pDAO.dajTokenProvjeri(korisnikHead, token);
		switch (tokenStatus) {
			case 0 : {
				odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Traženi token ne postoji ili nije Vaš").build();
			} break;
			case 1 : {
				odgovor = Response.status(Response.Status.REQUEST_TIMEOUT).entity("Token je istekao.").build();
			} break;
			case 2 : {
				KorisnikDAO kDAO = new KorisnikDAO(konfig);
				List<Grupa> grupe = kDAO.dohvatiGrupe(korisnik, konfig);
				
				if(!grupe.isEmpty()) {
					odgovor = Response.status(Response.Status.OK).entity(grupe).build();
				} else {
					odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podatka.").build();
				}
			}
		}
		return odgovor;
	}
}
