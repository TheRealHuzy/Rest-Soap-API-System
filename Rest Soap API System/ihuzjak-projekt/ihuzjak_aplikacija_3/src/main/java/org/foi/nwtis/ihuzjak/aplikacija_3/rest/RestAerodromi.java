package org.foi.nwtis.ihuzjak.aplikacija_3.rest;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.AerodromiDolasciDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.AerodromiPolasciDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.AerodromiPraceniDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.AerodromiUdaljenostDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.AerodromDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.DAO.ProvjeraDAO;
import org.foi.nwtis.ihuzjak.aplikacija_3.podaci.AerodromPracen;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("aerodromi")
public class RestAerodromi {
	
	@Inject
    ServletContext context;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajSveAerodrome(@HeaderParam("korisnik") String korisnik, @HeaderParam("token") String token) {
		
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
				AerodromDAO aDAO = new AerodromDAO(konfig);
				List<Airport> airporti = aDAO.dajSveAerodrome();
				List<Aerodrom> aerodromi = pretvoriAirportUAerodrom(airporti);
				if(!aerodromi.isEmpty()) {
					odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
				} else {
					odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema aerodroma.").build();
				}
			} break;
		}
		return odgovor;
	}
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response dodajAerodrom(@HeaderParam("icao") String icao,
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
			case 2: {
				AerodromiPraceniDAO apDAO = new AerodromiPraceniDAO(konfig);
				if (apDAO.posaljiAerodrom(icao)) {
					odgovor = Response.status(Response.Status.OK).entity("Aerodrom je unesen u bazu.").build();
				}
				else {
					odgovor = Response.status(Response.Status.NOT_FOUND).entity("Aerodrom nije unesen u bazu.").build();
				}
			} break;
		}
		return odgovor;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{icao}")
	public Response dajAerodrom(@PathParam("icao") String icao,
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
			case 2: {
				AerodromDAO aDAO = new AerodromDAO(konfig);
				List<Airport> airporti = aDAO.dajAerodrom(icao);
				List<Aerodrom> aerodromi = pretvoriAirportUAerodrom(airporti);
				
				if(!aerodromi.isEmpty()) {
					odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
				} else {
					odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema aerodroma.").build();
				}
			} break;
		}
		return odgovor;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{icao}/polasci")
	public Response dajPolaskeAerodoma(@PathParam("icao") String icao, @QueryParam("vrsta") int vrsta,
			@QueryParam("od") String vrijemeOd, @QueryParam("do") String vrijemeDo,
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
			case 2: {
				AerodromiPolasciDAO apDAO = new AerodromiPolasciDAO(konfig);
				List<AvionLeti> aerodromi = apDAO.dajAerodromePolaske(icao, vrijemeOd, vrijemeDo, vrsta);
				
				if(!aerodromi.isEmpty()) {
					odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
				} else {
					odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka.").build();
				}
			}
		}
		return odgovor;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{icao}/dolasci")
	public Response dajDolaskeAerodoma(@PathParam("icao") String icao, @QueryParam("vrsta") int vrsta,
			@QueryParam("od") String vrijemeOd, @QueryParam("do") String vrijemeDo,
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
			case 2: {
				AerodromiDolasciDAO adDAO = new AerodromiDolasciDAO(konfig);
				List<AvionLeti> aerodromi = adDAO.dajAerodromeDolaske(icao, vrijemeOd, vrijemeDo, vrsta);
				
				if(!aerodromi.isEmpty()) {
					odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
				} else {
					odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka.").build();
				}
			}
		}
		return odgovor;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{icao1}/{icao2}")
	public Response dajUdaljenost(@PathParam("icao1") String icao1, @PathParam("icao2") String icao2,
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
			case 2: {
				AerodromiUdaljenostDAO auDAO = new AerodromiUdaljenostDAO(konfig);
				String udaljenost = auDAO.dajUdaljenost(icao1, icao2);
				
				if(udaljenost.startsWith("udaljenost")) {
					odgovor = Response.status(Response.Status.OK).entity(udaljenost).build();
				} else {
					odgovor = Response.status(Response.Status.BAD_REQUEST).entity(udaljenost).build();
				}
			}
		}
		return odgovor;
	}
	
	@GET
	@Path("praceni/broj")
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajBrojPracenihZapisa() {
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		AerodromiPraceniDAO apDAO = new AerodromiPraceniDAO(konfig);
		int broj = apDAO.dajBrojPracenihAerodroma();
		
		if(broj != 0) {
			odgovor = Response.status(Response.Status.OK).entity(broj).build();
		} else {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podatka.").build();
		}
		return odgovor;
	}
	
	@GET
	@Path("praceni")
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajPraceneAerodrome() {
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		Response odgovor = null;
		
		AerodromiPraceniDAO apDAO = new AerodromiPraceniDAO(konfig);
		List<AerodromPracen> aerodromi = apDAO.dajSvePraceneAerodrome();
		
		if(!aerodromi.isEmpty()) {
			odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
		} else {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka.").build();
		}
		return odgovor;
	}
	
	private List<Aerodrom> pretvoriAirportUAerodrom(List<Airport> airporti){
		
		List<Aerodrom> aerodromi = new ArrayList<>();
		for (Airport a : airporti) {
			Aerodrom aerodrom = new Aerodrom();
			aerodrom.setNaziv(a.getName());
			aerodrom.setIcao(a.getIdent());
			aerodrom.setDrzava(a.getIso_country());
			aerodrom.setLokacija(dajLokaciju(a));
			aerodromi.add(aerodrom);
		}
		return aerodromi;
	}
	
	private Lokacija dajLokaciju(Airport a) {
		
		Lokacija l = new Lokacija();
		String[] s = a.getCoordinates().split(",");
		l.setLongitude(s[0].trim());
		l.setLatitude(s[1].trim());
		return l;
	}
}
