package org.foi.nwtis.ihuzjak.modul_6.podaci;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
@Entity
public class Korisnik {

	@Id
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
