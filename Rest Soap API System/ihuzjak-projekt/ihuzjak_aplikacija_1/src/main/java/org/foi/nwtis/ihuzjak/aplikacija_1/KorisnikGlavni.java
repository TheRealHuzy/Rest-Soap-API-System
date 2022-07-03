package org.foi.nwtis.ihuzjak.aplikacija_1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

public class KorisnikGlavni {

	static String komanda = "";
	static String adresaServera = "localhost";
	static int portSevera = 8002;
	static int maksCekanje;
	
	public static void main(String[] args) {
		
		KorisnikGlavni kg = new KorisnikGlavni();
		komanda = stvoriString(args);
		if (komanda == "") {
			System.out.println("ERROR 1 >> Komanda je neispravna!");
			return;
		}
		String odgovor = kg.posaljiKomandu(adresaServera, portSevera, komanda);
		System.out.println(odgovor);
	}

	private static String stvoriString(String[] args) {
		
		String odgovor = "";
		for (String s : args) {
			odgovor += s + " ";
		}
		return odgovor.trim();
	}

	public String posaljiKomandu(String adresa, int port, String komanda) {
		
		InetSocketAddress isa = new InetSocketAddress(adresa, port);
		try (Socket veza = new Socket())
		{
			veza.connect(isa, maksCekanje);
			InputStreamReader isr = new InputStreamReader(veza.getInputStream(),Charset.forName("UTF-8"));
			OutputStreamWriter osw = new OutputStreamWriter(veza.getOutputStream(),Charset.forName("UTF-8"));
			osw.write(komanda);
			osw.flush();
			veza.shutdownOutput();
			StringBuilder tekst = new StringBuilder();
			while (true) {
				int i = isr.read();
				if (i == -1) {
					break;
				}
				tekst.append((char) i);
			}
			veza.shutdownInput();
			veza.close();
			return tekst.toString();
		} catch (IOException e) {
			System.out.println("ERROR 2 >> Problem sa spajanjem na server!");
		}
		return null;
	}
}
