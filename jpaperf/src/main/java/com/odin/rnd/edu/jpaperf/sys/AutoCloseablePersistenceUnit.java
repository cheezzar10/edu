package com.odin.rnd.edu.jpaperf.sys;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AutoCloseablePersistenceUnit implements AutoCloseable {
	private static final String PERSISTENT_UNIT_NAME = "jpaperf";
	
	private final EntityManagerFactory emf;
	
	public AutoCloseablePersistenceUnit() {
		emf = Persistence.createEntityManagerFactory(PERSISTENT_UNIT_NAME);
	}
	
	public EntityManagerFactory get() {
		return emf;
	}
	
	@Override
	public void close() throws Exception {
		emf.close();
	}
}
