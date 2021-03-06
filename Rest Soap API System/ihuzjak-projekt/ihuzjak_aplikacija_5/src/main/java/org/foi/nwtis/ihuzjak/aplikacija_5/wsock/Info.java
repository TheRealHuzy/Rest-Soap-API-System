package org.foi.nwtis.ihuzjak.aplikacija_5.wsock;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/info")
public class Info {
	public static Set<Session> sesije = new HashSet<>();
	
	public static void informiraj(String poruka) {
		for(Session sesija: sesije) {
			if(sesija.isOpen()) {
				try {
					System.out.println("Saljem na "+ sesija.getId() +" >> " + poruka);
					sesija.getBasicRemote().sendText(poruka);
				} catch (IOException e) {
					System.out.println("Pogreška kod slanja poruke za sesiju: " + sesija.getId());
				}
			}
		}
	}
	
	@OnOpen
	public void otvori(Session sesija, EndpointConfig konfig) {
		sesije.add(sesija);
		System.out.println("Otvorena veza: " + sesija.getId());
	}

	@OnClose
	public void zatvori(Session sesija, CloseReason razlog) {
		sesije.remove(sesija);
		System.out.println("Zatvorena veza: " + sesija.getId() + " Razlog: " + razlog.getReasonPhrase());
	}

	@OnMessage
	public void stiglaPoruka(Session sesija, String poruka) {
		System.out.println("Veza: " + sesija.getId() + " Poruka: " + poruka);
	}

	@OnError
	public void pogreska(Session sesija, Throwable iznimka) {
		System.out.println("Veza: " + sesija.getId() + " Pogreška: " + iznimka.getMessage());
	}
}
