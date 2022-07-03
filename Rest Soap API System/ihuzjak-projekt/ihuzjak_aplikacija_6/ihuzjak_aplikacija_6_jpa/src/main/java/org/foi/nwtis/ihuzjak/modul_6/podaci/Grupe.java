package org.foi.nwtis.ihuzjak.modul_6.podaci;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor()
public class Grupe {

	@Id
    @Getter
    @Setter
    public
    String grupa;

    @Getter
    @Setter
    public
    String naziv;
}
