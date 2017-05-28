package com.odin.rnd.edu.jpaperf.demo;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.odin.rnd.edu.jpaperf.model.Account;
import com.odin.rnd.edu.jpaperf.sys.AutoCloseableEntityManager;
import com.odin.rnd.edu.jpaperf.sys.AutoCloseablePersistenceUnit;

public class LazyLoadingDemo {
	public static void main(String[] args) throws Exception {
		try (AutoCloseablePersistenceUnit emf = new AutoCloseablePersistenceUnit()) {
			Integer resellerId = null;
			try (AutoCloseableEntityManager em = new AutoCloseableEntityManager(emf.get())) {
				resellerId = init(em.get());
			}
			
			try (AutoCloseableEntityManager em = new AutoCloseableEntityManager(emf.get())) {
				load(em.get(), resellerId);
			}
		}
	}

	private static void load(EntityManager em, Integer resellerId) {
		Account reseller = em.find(Account.class, resellerId);
		Account provider = reseller.getOwner();
		
		if (provider != null) {
			System.out.printf("provider instance class: %s%n", provider.getClass().getName());
			Integer providerId = provider.getId();
			System.out.printf("provider id = %d%n", providerId);
			String companyName = provider.getCompanyName();
			System.out.printf("provider company name: %s%n", companyName);
		}
	}

	private static Integer init(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Account provider = new Account("Provider");
		em.persist(provider);
		
		Account reseller = new Account("Reseller");
		reseller.setOwner(provider);
		em.persist(reseller);
		
		tx.commit();
		
		return reseller.getId();
	}
}
