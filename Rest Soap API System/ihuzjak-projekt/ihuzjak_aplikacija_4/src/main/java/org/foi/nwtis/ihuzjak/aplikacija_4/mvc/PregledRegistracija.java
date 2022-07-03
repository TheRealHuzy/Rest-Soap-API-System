package org.foi.nwtis.ihuzjak.aplikacija_4.mvc;

import org.foi.nwtis.ihuzjak.aplikacija_4.RAO.KorisniciRAO;
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
@Path("registracija")
@RequestScoped
public class PregledRegistracija {

	@Inject
	private Models model;
	
	@Inject
	ServletContext context;
	
	String brojZapisa;

	@GET
	@View("registracijaJSP.jsp")
	public void registracijaUcitavanje() {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		String korisnikReg = konfig.dajPostavku("sustav.korisnik");
		String lozinkaReg = konfig.dajPostavku("sustav.lozinka");
		
		model.put("porukaReg", null);
		model.put("korisnikReg", korisnikReg);
		model.put("lozinkaReg", lozinkaReg);
	}
	
	@POST
	@View("registracijaJSP.jsp")
	public void registracijaSlanje(@FormParam("korisnik") String korisnik, @FormParam("lozinka") String lozinka,
			@FormParam("ime") String ime, @FormParam("prezime") String prezime, @FormParam("email") String email) {
		
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("Postavke");
		KorisniciRAO kRAO = new KorisniciRAO();
		try {
			kRAO.dodajKorisnika(korisnik, lozinka, ime, prezime, email);
			model.put("porukaReg", "Korisnik je evidentiran!");
		} catch (Exception e) {
			model.put("porukaReg", "Korisnik je već registriran ili nisu uneseni svi podaci!");
		}
		
		String korisnikReg = konfig.dajPostavku("sustav.korisnik");
		String lozinkaReg = konfig.dajPostavku("sustav.lozinka");

		model.put("korisnikReg", korisnikReg);
		model.put("lozinkaReg", lozinkaReg);
	}
}
