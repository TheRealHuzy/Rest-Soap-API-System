package org.foi.nwtis.ihuzjak.aplikacija_5.ws;

import java.util.List;

import org.foi.nwtis.ihuzjak.aplikacija_5.RAO.AerodromRAO;
import org.foi.nwtis.ihuzjak.aplikacija_5.wsock.Info;
import org.foi.nwtis.rest.podaci.AvionLeti;

import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceContext;

@WebService(serviceName = "aerodromi")
public class WsAerodromi {
	
	@Resource
	private WebServiceContext wsContext;

	@WebMethod
	public List<AvionLeti> dajPolaskeDan(@WebParam(name="korisnik") String korisnik, @WebParam(name="token") String token,
			@WebParam(name="icao") String icao, @WebParam(name="danOd") String danOd,
			@WebParam(name="danDo") String danDo) {
		
		AerodromRAO aRAO = new AerodromRAO();
		return aRAO.dajPolaskeDan(korisnik, token, icao, danOd, danDo);
	}
	
	@WebMethod
	public List<AvionLeti> dajPolaskeVrijeme(@WebParam(name="korisnik") String korisnik, @WebParam(name="token") String token,
			@WebParam(name="icao") String icao, @WebParam(name="danOd") String danOd,
			@WebParam(name="danDo") String danDo) {
		
		AerodromRAO aRAO = new AerodromRAO();
		return aRAO.dajPolaskeVrijeme(korisnik, token, icao, danOd, danDo);
	}
	
	@WebMethod
	public boolean dodajAerodromPreuzimanje(@WebParam(name="korisnik") String korisnik, @WebParam(name="token") String token,
			@WebParam(name="icao") String icao) {
		
		AerodromRAO aRAO = new AerodromRAO();
		Info.informiraj("Dodan je aerodrom >> " + icao + "!");
		return aRAO.dodajAerodromPreuzimanje(korisnik, token, icao);
	}
}
