package org.foi.nwtis.ihuzjak.modul_6.podaci;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
@Entity
public class Uloge {

	@Id
    @Getter
    @Setter
    public
    String korisnik;

    @Getter
    @Setter
    public
    String grupa;
}
