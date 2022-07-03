package org.foi.nwtis.ihuzjak.modul_6.zrna;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.ihuzjak.modul_6.RAO.AerodromiRAO;
import org.foi.nwtis.ihuzjak.modul_6.RAO.KorisniciRAO;
import org.foi.nwtis.ihuzjak.modul_6.podaci.AerodromPracen;
import org.foi.nwtis.ihuzjak.modul_6.podaci.Korisnik;
import org.foi.nwtis.ihuzjak.ws.aerodromi.Aerodromi;
import org.foi.nwtis.ihuzjak.ws.aerodromi.WsAerodromi;
import org.foi.nwtis.ihuzjak.ws.meteo.Meteo;
import org.foi.nwtis.ihuzjak.ws.meteo.MeteoPodaci;
import org.foi.nwtis.ihuzjak.ws.meteo.WsMeteo;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.xml.ws.WebServiceRef;
import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Named("AplikacijaWs")
public class AplikacijaWs {

	@WebServiceRef(wsdlLocation =
	"http://localhost:9090/ihuzjak_aplikacija_5/aerodromi?wsdl")
	private Aerodromi service;
	
	@WebServiceRef(wsdlLocation =
	"http://localhost:9090/ihuzjak_aplikacija_5/meteo?wsdl")
	private Meteo meteoService;
	
	//---prijava
	@Inject
	ServletContext context;

	@Getter
	private String korisnik = "";
	@Getter
	private String lozinka = "";
	@Getter
	private String token = "";
	@Getter
	private boolean jeLiPrijavljen = false;
	@Getter
	@Setter
	String poruka = "";
	@Setter
	private boolean postojiToken = false;

	public void setKorisnik(String korisnik) {
		this.korisnik = korisnik;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setJeLiPrijavljen(boolean jeLiPrijavljen) {
		this.jeLiPrijavljen = jeLiPrijavljen;
	}
	public boolean getPostojiToken() {
		if (context.getAttribute("token") == null) {
			context.setAttribute("token", "");
		}
		return context.getAttribute("token").toString().compareTo("") != 0;
	}

	public void prijaviSe() {
		KorisniciRAO kRAO = new KorisniciRAO(dajKonfig());
		String token = kRAO.dajToken(korisnik, lozinka);
		if (token != null) {
			this.token = token;
			jeLiPrijavljen = true;
			poruka = "";
			context.setAttribute("token", token);
			context.setAttribute("korisnik", korisnik);
			context.setAttribute("lozinka", lozinka);
		} else {
			poruka = "Netočno korisničko ime ili lozinka!";
		}
	}

	public void odjaviSe() {
		KorisniciRAO kRAO = new KorisniciRAO(dajKonfig());
		
		kRAO.brisiMojToken(context.getAttribute("token").toString(), context.getAttribute("korisnik").toString(),
				context.getAttribute("lozinka").toString());
		context.setAttribute("token", "");
		context.setAttribute("korisnik", "");
		context.setAttribute("lozinka", "");
		this.jeLiPrijavljen = false;
		poruka = "Odjavljeni ste!";
	}

	//---korisnici
	@Setter
	private List<Korisnik> korisnici = new ArrayList<>();

	public List<Korisnik> getKorisnici() {

		this.korisnici = this.dajSveKorisnike();
		return this.korisnici;
	}

	public List<Korisnik> dajSveKorisnike() {

		KorisniciRAO kRAO = new KorisniciRAO(dajKonfig());
		return kRAO.dajKorisnike(context.getAttribute("korisnik").toString(),
				context.getAttribute("token").toString());
	}

	private Konfiguracija dajKonfig() {
		return (Konfiguracija) context.getAttribute("Postavke");
	}
	
	//---aerodromi
	@Setter
	private List<Aerodrom> aerodromi = new ArrayList<>();
	
	public List<Aerodrom> getAerodromi() {

		this.aerodromi = this.dajSveAerodrome();
		return this.aerodromi;
	}
	
	public List<Aerodrom> dajSveAerodrome() {
		
		AerodromiRAO aRAO = new AerodromiRAO(dajKonfig());
		return aRAO.dajAerodrome(context.getAttribute("korisnik").toString(),
				context.getAttribute("token").toString());
	}
	/*
	@Setter
	private List<Aerodrom> praceni = new ArrayList<>();
	
	public List<Aerodrom> getPraceni() {

		this.praceni = this.dajSvePracene();
		return this.praceni;
	}
	
	public List<Aerodrom> dajSvePracene() {
		
		AerodromiRAO aRAO = new AerodromiRAO(dajKonfig());
		List<AerodromPracen> aerodromiPraceni = aRAO.dajPracene(context.getAttribute("korisnik").toString(),
				context.getAttribute("token").toString());
		List<Aerodrom> aerodromi = new ArrayList<>();
		for (AerodromPracen a : aerodromiPraceni) {
			Aerodrom aerodrom = aRAO.dajAerodrom(a.getIdent(), context.getAttribute("korisnik").toString(),
				context.getAttribute("token").toString());
			aerodromi.add(aerodrom);
		}
		return aerodromi;
	}*/
	
	@Setter
	private List<AerodromPracen> praceni = new ArrayList<>();
	
	public List<AerodromPracen> getPraceni() {

		this.praceni = this.dajSvePracene();
		return this.praceni;
	}
	
	public List<AerodromPracen> dajSvePracene() {
		
		AerodromiRAO aRAO = new AerodromiRAO(dajKonfig());
		List<AerodromPracen> aerodromiPraceni = aRAO.dajPracene(context.getAttribute("korisnik").toString(),
				context.getAttribute("token").toString());
		return aerodromiPraceni;
	}
	
	@Getter
	private String aerodrom;
	
	public void setAerodrom(String aerodrom) {
		this.aerodrom = aerodrom;
	}
	
	public void dodajAerodrom() {
		AerodromiRAO aRAO = new AerodromiRAO(dajKonfig());
		aRAO.dodajAerodrom(aerodrom, context.getAttribute("korisnik").toString(),
				context.getAttribute("token").toString());
	}
	
	public void dodajAerodromWeb() {
		service = new Aerodromi();
		WsAerodromi wsAerodromi = service.getWsAerodromiPort();
		wsAerodromi.dodajAerodromPreuzimanje(context.getAttribute("korisnik").toString(),
				context.getAttribute("token").toString(), aerodrom);
	}
	
	//---polasci/dolasci
	
	@Getter
	private String icao;
	@Getter
	private String vrijemeOd;
	@Getter
	private String vrijemeDo;
	
	public void setIcao(String icao) {
		this.icao = icao;
	}
	public void setVrijemeOd(String vrijemeOd) {
		this.vrijemeOd = vrijemeOd;
	}
	public void setVrijemeDo(String vrijemeDo) {
		this.vrijemeDo = vrijemeDo;
	}
	
	@Setter
	private List<AvionLeti> dpolasci = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public List<AvionLeti> getDpolasci() {
		
		dpolasci = (List<AvionLeti>) context.getAttribute("dpolasci");
		return this.dpolasci;
	}
	
	public List<AvionLeti> dajDolaskeDatum() {
		AerodromiRAO aRAO = new AerodromiRAO(dajKonfig());
		 List<AvionLeti> al = aRAO.dajDpolaskeDatumLong(icao, "0", "dolasci", vrijemeOd, vrijemeDo,
				context.getAttribute("korisnik").toString(), context.getAttribute("token").toString());
		 context.setAttribute("dpolasci", al);
		 return al;
	}
	public List<AvionLeti> dajDolaskeLong() {
		AerodromiRAO aRAO = new AerodromiRAO(dajKonfig());
		List<AvionLeti> al = aRAO.dajDpolaskeDatumLong(icao, "1", "dolasci", vrijemeOd, vrijemeDo,
				context.getAttribute("korisnik").toString(), context.getAttribute("token").toString());
		context.setAttribute("dpolasci", al);
		return al;
	}
	public List<AvionLeti> dajPolaskeDatum() {
		AerodromiRAO aRAO = new AerodromiRAO(dajKonfig());
		List<AvionLeti> al = aRAO.dajDpolaskeDatumLong(icao, "0", "polasci", vrijemeOd, vrijemeDo,
				context.getAttribute("korisnik").toString(), context.getAttribute("token").toString());
		context.setAttribute("dpolasci", al);
		return al;
	}
	public List<AvionLeti> dajPolaskeLong() {
		AerodromiRAO aRAO = new AerodromiRAO(dajKonfig());
		List<AvionLeti> al = aRAO.dajDpolaskeDatumLong(icao, "1", "polasci", vrijemeOd, vrijemeDo,
				context.getAttribute("korisnik").toString(), context.getAttribute("token").toString());
		context.setAttribute("dpolasci", al);
		return al;
	}
	
	//---meteo
	
	@Getter
	private String aerodromMeteo;
	
	public void setAerodromMeteo(String aerodromMeteo) {
		this.aerodromMeteo = aerodromMeteo;
	}
	
	@Setter
	MeteoPodaci meteoPodaci;
	
	public MeteoPodaci getMeteoPodaci() {
		
		meteoPodaci = (MeteoPodaci) context.getAttribute("meteo");
		return this.meteoPodaci;
	}

	public MeteoPodaci dajMeteo() {
		
		meteoService = new Meteo();
		WsMeteo wsMeteo = meteoService.getWsMeteoPort();
		MeteoPodaci meteoPodaci = wsMeteo.dajMeteo(aerodromMeteo, context.getAttribute("korisnik").toString(),
				context.getAttribute("token").toString());
		context.setAttribute("meteo", meteoPodaci);
		return meteoPodaci;
	}
}
