package org.foi.nwtis.ihuzjak.aplikacija_4.RAO;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class ServeriRAO {

	public String dajSveAerodrome(String korisnik, String token) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/ihuzjak_aplikacija_3/api/aerodromi");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.header("korisnik", korisnik)
				.header("token", token)
				.get();
		String odgovor = null;
		if (restOdgovor.getStatus() == 200) {
			odgovor = restOdgovor.readEntity(String.class);
		}
		return odgovor;
	}
	
	public String posaljikomandu(String adresa, int port, String komanda) {
		
		int maksCekanje = 5000;
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
			System.out.println("INFO INTERNAL AP4 >> Problem sa spajanjem na server udaljenosti!");
		}
		return null;
	}
}
