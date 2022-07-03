package org.foi.nwtis.ihuzjak.aplikacija_2.slusaci;

import org.foi.nwtis.ihuzjak.aplikacija_2.dretve.PreuzimanjeRasporedaAerodroma;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.NeispravnaKonfiguracija;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SlusacAplikacije implements ServletContextListener {
	PreuzimanjeRasporedaAerodroma pra = null;
	Konfiguracija konfig = null;

	String vrijemePocetka;
	String vrijemeKraja;
	String putanja;
	
    public SlusacAplikacije(){}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ServletContext context = sce.getServletContext();
		String nazivDatoteke = context.getInitParameter("konfiguracija");
		putanja = context.getRealPath("/WEB-INF") + java.io.File.separator; 
		
		try {
			konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanja + nazivDatoteke);
			konfig.ucitajKonfiguraciju();
			context.setAttribute("Postavke", konfig);
		} 
		catch (NeispravnaKonfiguracija e1) {
			e1.printStackTrace();
			return;
		}

		pra = new PreuzimanjeRasporedaAerodroma(konfig);
		pra.start();
		ServletContextListener.super.contextInitialized(sce);
	}
	

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		ServletContext context = sce.getServletContext();
		context.removeAttribute("Postavke");
		if(pra != null) {
			pra.interrupt();
		}
		ServletContextListener.super.contextDestroyed(sce);
	}
}
