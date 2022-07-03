package org.foi.nwtis.ihuzjak.modul_6.JPA;

import java.util.List;

import org.foi.nwtis.ihuzjak.modul_6.podaci.Korisnik;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public class TODO_JPA {
	
	public List<Korisnik> funkcija() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("NWTiS_ihuzjak_PU");
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Korisnik> cq = cb.createQuery(Korisnik.class);
		cq.select(cq.from(Korisnik.class));
		return em.createQuery(cq).getResultList();
	}
}
