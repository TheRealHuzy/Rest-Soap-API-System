package org.foi.nwtis.ihuzjak.aplikacija_4.mvc;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.ihuzjak.aplikacija_4.RAO.KorisniciRAO;
import org.foi.nwtis.ihuzjak.aplikacija_4.podaci.Korisnik;

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
@Path("korisnici")
@RequestScoped
public class PregledKorisnika {

	@Inject
	Models model;
	
	@Inject
	ServletContext context;

	@GET
	@Path("pocetna")
	@View("pocetnaJSP.jsp")
	public void pocetak() {
		if (context.getAttribute("token") == null) {
			model.put("odjava", true);
		} else {
			model.put("odjava", false);
		}
	}
	
	@POST
	@Path("pocetna")
	@View("pocetnaJSP.jsp")
	public void odjava() {
		
		KorisniciRAO kRAO = new KorisniciRAO();
		kRAO.brisiMojToken(context.getAttribute("token").toString(), context.getAttribute("korisnik").toString(),
				context.getAttribute("lozinka").toString());
		
		context.setAttribute("korisnik", null);
		context.setAttribute("lozinka", null);
		context.setAttribute("token", null);
		model.put("odjava", true);
	}

	@GET
	@View("korisniciJSP.jsp")
	public void dajSveKorisnike() {
		
		List<Korisnik> korisnici = new ArrayList<>();
		boolean admin = false;
		model.put("brisanjeTokena", false);
		model.put("porukaKor", null);
		model.put("brisanjeTokena", false);
		
		try {
			KorisniciRAO kRAO = new KorisniciRAO();
			korisnici = kRAO.dajSveKorisnike(context.getAttribute("korisnik").toString(),
					context.getAttribute("token").toString());
			admin = kRAO.jesamLiAdmin(context.getAttribute("korisnik").toString());
		} catch (Exception e) {
			model.put("porukaKor", "Niste prijavljeni");
		}
		model.put("admin", admin);
		model.put("korisnici", korisnici);
	}
	
	@POST
	@View("korisniciJSP.jsp")
	public void brisiKorisnika(@FormParam("korisnikZaBrisanje") String korisnikZaBrisanje,
			@FormParam("operacija") String operacija) {
		
		List<Korisnik> korisnici = new ArrayList<>();
		boolean admin = false;
		model.put("porukaKor", null);
		model.put("brisanjeTokena", false);
		
		try {
			KorisniciRAO kRAO = new KorisniciRAO();
			korisnici = kRAO.dajSveKorisnike(context.getAttribute("korisnik").toString(),
					context.getAttribute("token").toString());
			admin = kRAO.jesamLiAdmin(context.getAttribute("korisnik").toString());
			switch (Integer.parseInt(operacija)) {
			case 0: {
				kRAO.brisiTokeneKorisnika(korisnikZaBrisanje, context.getAttribute("korisnik").toString(),
						context.getAttribute("lozinka").toString());
			} break;
			case 1: {
				kRAO.brisiMojToken(context.getAttribute("token").toString(), context.getAttribute("korisnik").toString(),
						context.getAttribute("lozinka").toString());
				context.setAttribute("korisnik", null);
				context.setAttribute("lozinka", null);
				context.setAttribute("token", null);
				model.put("odjava", true);
				model.put("brisanjeTokena", true);
				model.put("porukaKor", "Obrisali ste svoj token!");
			} break;
		}
		} catch (Exception e) {
			model.put("porukaKor", "Niste prijavljeni!");
		}	
		model.put("admin", admin);
		model.put("korisnici", korisnici);
	}
}
