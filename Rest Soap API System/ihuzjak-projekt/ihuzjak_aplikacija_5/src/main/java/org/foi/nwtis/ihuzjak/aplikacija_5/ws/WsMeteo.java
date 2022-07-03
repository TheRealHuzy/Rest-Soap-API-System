package org.foi.nwtis.ihuzjak.aplikacija_5.ws;

import org.foi.nwtis.ihuzjak.aplikacija_5.RAO.AerodromRAO;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.handler.MessageContext;

@WebService(serviceName = "meteo")
public class WsMeteo {
	
	@Resource
	private WebServiceContext wsContext;
	
	@WebMethod
	public MeteoPodaci dajMeteo(@WebParam(name="icao") String icao, @WebParam(name="korisnik") String korisnik,
			@WebParam(name="token") String token) {
		
		Konfiguracija konf = dajKonfig();
		AerodromRAO aRAO = new AerodromRAO();
		Aerodrom aerodrom = aRAO.dajAerodrom(icao, korisnik, token);
		return izvrsiMeteo(konf, aerodrom);
	}
	
	private MeteoPodaci izvrsiMeteo(Konfiguracija konf, Aerodrom a) {
		
		OWMKlijent owmKlijent = new OWMKlijent(konf.dajPostavku("OpenWeatherMap.apikey"));
		MeteoPodaci meteo = new MeteoPodaci();
		
		try {
			meteo = owmKlijent.getRealTimeWeather(a.getLokacija().getLatitude(), a.getLokacija().getLongitude());
		} catch (Exception e) {
			System.out.println("Greška u razgovoru sa OWM klijentom");
		}
		return meteo;
	}

	private Konfiguracija dajKonfig() {
		 
		 ServletContext context = (ServletContext) wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		 Konfiguracija konf = (Konfiguracija) context.getAttribute("Postavke");
		 return konf;
	 }
}
