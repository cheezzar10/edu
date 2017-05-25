package com.odin.rnd.edu.jpaperf.sys;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AutoCloseableEntityManager implements AutoCloseable {
	private static final String PERSISTENT_UNIT_NAME = "jpaperf";
	
	private final EntityManagerFactory emf;
	private final EntityManager em;

	public AutoCloseableEntityManager() {
		this.emf = Persistence.createEntityManagerFactory(PERSISTENT_UNIT_NAME);
		this.em = emf.createEntityManager();
	}
	
	public EntityManager get() {
		return em;
	}
	
	public void close() {
		try {
			em.close();
		} catch (Exception closeEx) {
			// ignore, proceed to emf closing
		}
		
		emf.close();
	}
}
