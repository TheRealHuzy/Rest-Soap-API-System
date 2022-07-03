package org.foi.nwtis.ihuzjak.aplikacija_3.rest;

import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.AerodromiUdaljenostDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.ProvjeraDAO;
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

@Path("serveri")
public class RestServeri {
	
	@Inject
    ServletContext context;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajStatus(@HeaderParam("korisnik") String korisnik, @HeaderParam("token") String token) {
		
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
				AerodromiUdaljenostDAO auDAO = new AerodromiUdaljenostDAO(konfig);
				String status = auDAO.dajStatus();
				
				if(!status.isEmpty()) {
					odgovor = Response.status(Response.Status.OK).entity("{adresa: " + auDAO.getAdresa()
						+ ", port: " + auDAO.getPort() + ", status: " + status.substring(3) + "}").build();
				} else {
					odgovor = Response.status(Response.Status.BAD_REQUEST).entity("Nepravilan zahtjev.").build();
				}
			}
		}
		return odgovor;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{komanda}")
	public Response posaljiKomandu(@PathParam("komanda") String komanda,
			@HeaderParam("korisnik") String korisnik, @HeaderParam("token") String token) {
		
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
				AerodromiUdaljenostDAO auDAO = new AerodromiUdaljenostDAO(konfig);
				String status = auDAO.posaljiKomandu(komanda);
				
				if(!status.startsWith("INFO") && !status.startsWith("ERROR")) {
					odgovor = Response.status(Response.Status.OK).entity(status).build();
				} else {
					odgovor = Response.status(Response.Status.BAD_REQUEST).entity(status).build();
				}
			}
		}
		return odgovor;
	}
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@Path("LOAD")
    public Response posaljiLoad(@HeaderParam("aerodromi") String aerodromi,
    		@HeaderParam("korisnik") String korisnik, @HeaderParam("token") String token) {
		
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
				AerodromiUdaljenostDAO auDAO = new AerodromiUdaljenostDAO(konfig);
				String odgovorServera = auDAO.posaljiLoadInner(aerodromi);
				
				if(!odgovorServera.startsWith("ERROR")) {
					odgovor = Response.status(Response.Status.OK).entity(odgovorServera).build();
				} else {
					odgovor = Response.status(Response.Status.CONFLICT).entity(odgovorServera).build();
				}
			}
		}
		return odgovor;
	}
}
