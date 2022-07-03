package org.foi.nwtis.ihuzjak.aplikacija_5.slusaci;

import org.foi.nwtis.ihuzjak.aplikacija_5.dretve.Osvjezivac;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.NeispravnaKonfiguracija;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class SlusacAplikacije
 *
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {
	Osvjezivac osvjezivac = null;
	
    public SlusacAplikacije() {
    }

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ServletContext context = sce.getServletContext();
		String nazivDatoteke = context.getInitParameter("konfiguracija");
		Konfiguracija konfig = null;
		String putanja = context.getRealPath("/WEB-INF") + java.io.File.separator;
		
		try {
			konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanja + nazivDatoteke);
			konfig.ucitajKonfiguraciju();
			context.setAttribute("Postavke", konfig);
		} catch (NeispravnaKonfiguracija e1) {
			e1.printStackTrace();
			return;
		}

		osvjezivac = new Osvjezivac(konfig);
		osvjezivac.start();
		ServletContextListener.super.contextInitialized(sce);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		ServletContext context = sce.getServletContext();
		context.removeAttribute("Postavke");
		osvjezivac.interrupt();
		ServletContextListener.super.contextDestroyed(sce);
	}

}
