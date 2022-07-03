package org.foi.nwtis.ihuzjak.aplikacija_3.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class Korisnik {

    @Getter
    @Setter
    public
    String korisnik;

    @Getter
    @Setter
    public
    String lozinka;
    
    @Getter
    @Setter
	public
    String prezime;

    @Getter
    @Setter
    public
    String ime;

    @Getter
    @Setter
    public
    String email;
}
