package com.odin.rnd.edu.jpaperf.demo;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.odin.rnd.edu.jpaperf.model.Account;
import com.odin.rnd.edu.jpaperf.sys.AutoCloseableEntityManager;

public class LazyLoadingDemo {
	public static void main(String[] args) {
		try (AutoCloseableEntityManager em = new AutoCloseableEntityManager()) {
			init(em.get());
		}
	}

	private static void init(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Account acc = new Account("Company 1");
		em.persist(acc);
		
		tx.commit();
	}
}
