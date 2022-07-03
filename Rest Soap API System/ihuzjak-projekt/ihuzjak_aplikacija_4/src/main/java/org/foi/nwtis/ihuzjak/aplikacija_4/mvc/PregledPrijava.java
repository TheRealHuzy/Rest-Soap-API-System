package org.foi.nwtis.ihuzjak.aplikacija_4.mvc;

import org.foi.nwtis.ihuzjak.aplikacija_4.RAO.KorisniciRAO;

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
@Path("prijava") //aeroCopy
@RequestScoped
public class PregledPrijava {

	@Inject
	private Models model;
	
	@Inject
	ServletContext context;
	
	String brojZapisa;

	@GET
	@View("prijavaJSP.jsp")
	public void registracijaUcitavanje() {
		
		model.put("porukaPri", null);
	}
	
	@POST
	@View("prijavaJSP.jsp")
	public void registracijaSlanje(@FormParam("korisnik") String korisnik, @FormParam("lozinka") String lozinka) {
		
		KorisniciRAO kRAO = new KorisniciRAO();
		String token = null;
		try {
			token = kRAO.dajToken(korisnik, lozinka);
			if (token != null) {
				context.setAttribute("korisnik", korisnik);
				context.setAttribute("lozinka", lozinka);
				context.setAttribute("token", token);
				model.put("porukaPri", "Uspješna prijava!");
			} else {
				model.put("porukaPri", "Neuspješna prijava!");
			}
		} catch (Exception e) {
			model.put("porukaPri", "Neuspješna prijava!");
		}
	}
}
