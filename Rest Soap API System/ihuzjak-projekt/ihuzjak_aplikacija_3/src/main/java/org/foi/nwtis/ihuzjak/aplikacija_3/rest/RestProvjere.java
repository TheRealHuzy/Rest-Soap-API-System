package org.foi.nwtis.ihuzjak.aplikacija_3.rest;

import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.ProvjeraDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.Token;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("provjere")
public class RestProvjere {
	
	@Inject
    ServletContext context;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajAutentifikaciju(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		ProvjeraDAO pDAO = new ProvjeraDAO(konfig);
		Token token = pDAO.dajAutentifikacijuNoviToken(korisnik, lozinka);
		if (token == null) {
			odgovor = Response.status(Response.Status.UNAUTHORIZED)
					.entity("Korisnik sa zadanim imenom i lozinkom ne postoji.").build();
		}
		else {
			String vraceniPodaci = "{token:" + token.getId() + ", vrijeme:" + token.getVrijediDo() + "}";
			odgovor = Response.status(Response.Status.OK).entity(vraceniPodaci).build();
		}
		return odgovor;
	}
	
	@GET
	@Path("{token}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajProvjeruTokena(@PathParam("token") String token,
			@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		ProvjeraDAO pDAO = new ProvjeraDAO(konfig);
		boolean autentifikacija = pDAO.dajAutentifikaciju(korisnik, lozinka);
		if (!autentifikacija) {
			odgovor = Response.status(Response.Status.UNAUTHORIZED)
					.entity("Korisnik sa zadanim imenom i lozinkom ne postoji.").build();
			return odgovor;
		}
		
		int tokenStatus = pDAO.dajTokenProvjeri(korisnik, token);
		switch (tokenStatus) {
			case 0 : {
				odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Traženi žeton ne postoji ili nije Vaš.").build();
			} break;
			case 1 : {
				odgovor = Response.status(Response.Status.REQUEST_TIMEOUT).entity("Token je istekao.").build();
			} break;
			case 2 : {
				odgovor = Response.status(Response.Status.OK).entity("Token je važeči.").build();
			} break;
		}
		return odgovor;
	}
	
	@DELETE
	@Path("{token}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajPonistiValjanostTokena(@PathParam("token") String token,
			@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		ProvjeraDAO pDAO = new ProvjeraDAO(konfig);
		boolean autentifikacija = pDAO.dajAutentifikaciju(korisnik, lozinka);
		if (!autentifikacija) {
			odgovor = Response.status(Response.Status.UNAUTHORIZED)
					.entity("Korisnik sa zadanim imenom i lozinkom ne postoji.").build();
			return odgovor;
		}
		
		int brisanjeTokena = pDAO.dajTokenIzbrisi(korisnik, token);
		switch (brisanjeTokena) {
			case 0 : {
				odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Traženi žeton nije Vaš ili ne postoji.").build();
			} break;
			case 1 : {
				odgovor = Response.status(Response.Status.REQUEST_TIMEOUT).entity("Token je istekao.").build();
			} break;
			case 2 : {
				odgovor = Response.status(Response.Status.OK).entity("Token je postaljven kao nevažeči.").build();
			} break;
		}
		return odgovor;
	}
	
	@DELETE
	@Path("korisnik/{korisnik}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajObrisiToken(@PathParam("korisnik") String korisnik,
			@HeaderParam("korisnikHead") String korisnikHead, @HeaderParam("lozinka") String lozinka) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		ProvjeraDAO pDAO = new ProvjeraDAO(konfig);
		boolean autentifikacija = pDAO.dajAutentifikaciju(korisnikHead, lozinka);
		if (!autentifikacija) {
			odgovor = Response.status(Response.Status.UNAUTHORIZED)
					.entity("Korisnik sa zadanim imenom i lozinkom ne postoji.").build();
			return odgovor;
		}
		
		int autorizacija = pDAO.dajAutorizacijuZaBrisanjeSvihTokena(korisnikHead, korisnik);
		switch (autorizacija) {
			case 0 : {
				odgovor = Response.status(Response.Status.UNAUTHORIZED)
						.entity("Korisnik nema potrebna prava za traženu akciju.").build();
			} break;
			case 1 : {
				odgovor = Response.status(Response.Status.NOT_FOUND).entity("Korisnik nema aktivnih tokena.").build();
			} break;
			case 2 : {
				odgovor = Response.status(Response.Status.OK).entity("Aktivni tokeni su postavljeni kao nevažeči.").build();
			} break;
		}
		return odgovor;
	}
	
	@GET
	@Path("admin")
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajAdmina(@HeaderParam("korisnik") String korisnik) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		ProvjeraDAO pDAO = new ProvjeraDAO(konfig);
		boolean admin = pDAO.dajAdminaInner(korisnik);
		if (!admin) {
			odgovor = Response.status(Response.Status.UNAUTHORIZED).entity(admin).build();
		} else {
			odgovor = Response.status(Response.Status.OK).entity(admin).build();
		}
		return odgovor;
	}
}
