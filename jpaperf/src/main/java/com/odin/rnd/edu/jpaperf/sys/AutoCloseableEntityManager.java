package com.odin.rnd.edu.jpaperf.sys;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AutoCloseableEntityManager implements AutoCloseable {
	private final EntityManager em;

	public AutoCloseableEntityManager(EntityManagerFactory emf) {
		this.em = emf.createEntityManager();
	}
	
	public EntityManager getEntityManager() {
		return em;
	}
	
	public void close() {
		em.close();
	}
}
