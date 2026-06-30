package com.saas.b2b.shared.tenancy;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class TenantHibernateFilterEnabler {

	@PersistenceContext
	private EntityManager entityManager;

	public void enable(Long tenantId) {
		if (tenantId == null) {
			return;
		}
		Session session = entityManager.unwrap(Session.class);
		session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
	}
}
