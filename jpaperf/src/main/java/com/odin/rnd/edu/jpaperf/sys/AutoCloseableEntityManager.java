package com.odin.rnd.edu.jpaperf.sys;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AutoCloseableEntityManager implements AutoCloseable {
	private final EntityManager em;

	public AutoCloseableEntityManager(EntityManagerFactory emf) {
		em = emf.createEntityManager();
	}
	
	public EntityManager get() {
		return em;
	}
	
	public void close() throws Exception {
		em.close();
	}
}
