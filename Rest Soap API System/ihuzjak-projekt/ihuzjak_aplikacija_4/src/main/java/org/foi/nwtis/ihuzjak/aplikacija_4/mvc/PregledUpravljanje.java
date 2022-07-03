package org.foi.nwtis.ihuzjak.aplikacija_4.mvc;

import java.util.HashMap;
import java.util.Map;

import org.foi.nwtis.ihuzjak.aplikacija_4.RAO.ServeriRAO;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Controller
@Path("server")
@RequestScoped
public class PregledUpravljanje {

	@Inject
	Models model;
	
	@Inject
	ServletContext context;
	
	String adresa;
	int port;

	@GET
	@View("upravljanjeServeromJSP.jsp")
	public void inicijalniPregled() {
		
		if (context.getAttribute("token") != null) {
			Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
			adresa = konfig.dajPostavku("server.udaljenosti.adresa");
			port = Integer.parseInt(konfig.dajPostavku("server.udaljenosti.port"));
			ServeriRAO sRAO = new ServeriRAO();
			String status;
			try {
				status = sRAO.posaljikomandu(adresa, port, "STATUS").substring(3);
			} catch (Exception e) {
				status = "Server je ugašen!";
			}
			
			Map<String, String> komande = new HashMap<String, String>();
			stvoriKomande(komande);
			
			model.put("odgovorServera", null);
			model.put("komande", komande);
			model.put("status", status);
			model.put("porukaSer", null);
		} else {
			model.put("porukaSer", "Niste prijavljeni!");
		}
	}
	
	@POST
	@View("upravljanjeServeromJSP.jsp")
	public void slanjeKomande(@FormParam("slcKomande") String slcKomande) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		adresa = konfig.dajPostavku("server.udaljenosti.adresa");
		port = Integer.parseInt(konfig.dajPostavku("server.udaljenosti.port"));
		
		Map<String, String> komande = new HashMap<String, String>();
		stvoriKomande(komande);
		String serverKomanda = odrediKomandu(komande, slcKomande);
		ServeriRAO sRAO = new ServeriRAO();
		if (serverKomanda.compareTo("LOAD") == 0) {
			serverKomanda += " " + sRAO.dajSveAerodrome(context.getAttribute("korisnik").toString(), context.getAttribute("token").toString());
		}
		
		String status;
		String odgovorServera;
		try {
			odgovorServera = sRAO.posaljikomandu(adresa, port, serverKomanda);
			status = sRAO.posaljikomandu(adresa, port, "STATUS").substring(3);
		} catch (Exception e) {
			status = "Server je ugašen!";
			odgovorServera = "INFO INTERNAL AP4 >> Problem sa spajanjem na server udaljenosti!";
		}
		
		model.put("odgovorServera", odgovorServera);
		model.put("komande", komande);
		model.put("status", status);
	}

	public void stvoriKomande(Map<String, String> komande) {
		
		komande.put("INIT", "Inicijalizacija poslužitelja");
		komande.put("QUIT", "Prekid rada poslužitelja");
		komande.put("LOAD", "Učitavanje podataka");
		komande.put("CLEAR", "Brisanje podataka");
	}
	
	public String odrediKomandu(Map<String, String> komande, String slcKomande) {
		
		String odgovor = null;
		for(Map.Entry<String, String> element : komande.entrySet()) {
		    if (element.getValue().compareTo(slcKomande) == 0) {
		    	odgovor = element.getKey();
		    	break;
		    }
		}
		return odgovor;
	}
}
