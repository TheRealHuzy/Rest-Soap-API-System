package org.foi.nwtis.ihuzjak.aplikacija_5.dretve;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.foi.nwtis.ihuzjak.aplikacija_5.RAO.AerodromRAO;
import org.foi.nwtis.ihuzjak.aplikacija_5.wsock.Info;
import org.foi.nwtis.ihuzjak.lib.konfiguracije.Konfiguracija;

public class Osvjezivac extends Thread {

	boolean kraj = false;
	int vrijemeSpavanja;
	
	Konfiguracija konf;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	public Osvjezivac(Konfiguracija konf) {
		
		this.konf = konf;
	}
	
	@Override
	public synchronized void start() {
		
		vrijemeSpavanja = Integer.parseInt(konf.dajPostavku("ciklus.spavanje"));
		super.start();
	}

	@Override
	public void run() {
		
		while(!kraj) {
			Timestamp tVrijemePocetka = new Timestamp(System.currentTimeMillis());
			String vrijeme = sdf.format(tVrijemePocetka).toString();
			int brojAerodroma = dajAerodromePracene();
			
			Info.informiraj(vrijeme + "," + brojAerodroma);
			
			try {
				Thread.sleep(vrijemeSpavanja*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void interrupt() {
		
		kraj = true;
		super.interrupt();
	}
	
	public int dajAerodromePracene() {
		
		AerodromRAO aRAO = new AerodromRAO();
		return Integer.parseInt(aRAO.dajBrojPracenihAerodroma());
	}
}
