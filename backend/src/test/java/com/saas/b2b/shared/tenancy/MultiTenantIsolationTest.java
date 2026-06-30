package com.saas.b2b.shared.tenancy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.saas.b2b.customer.application.port.out.CustomerRepositoryPort;
import com.saas.b2b.customer.domain.model.Customer;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MultiTenantIsolationTest {

	@Autowired
	private CustomerRepositoryPort customerRepository;

	@Autowired
	private TenantHibernateFilterEnabler tenantHibernateFilterEnabler;

	@AfterEach
	void tearDown() {
		TenantContext.clear();
	}

	@Test
	void tenant1VeApenasSeusClientes() {
		TenantContext.setCurrentTenantId(1L);
		tenantHibernateFilterEnabler.enable(1L);

		var customers = customerRepository.findAllByTenantId(1L);

		assertThat(customers).hasSize(2);
		assertThat(customers).extracting(Customer::getTenantId).containsOnly(1L);
	}

	@Test
	void tenant2VeApenasSeusClientes() {
		TenantContext.setCurrentTenantId(2L);
		tenantHibernateFilterEnabler.enable(2L);

		var customers = customerRepository.findAllByTenantId(2L);

		assertThat(customers).hasSize(2);
		assertThat(customers).extracting(Customer::getTenantId).containsOnly(2L);
	}

	@Test
	void tenant1NaoAcessaClienteDeOutroTenantPorId() {
		TenantContext.setCurrentTenantId(1L);
		tenantHibernateFilterEnabler.enable(1L);

		assertThat(customerRepository.findById(3L)).isEmpty();
	}

	@Test
	void tenant2NaoAcessaClienteDeOutroTenantPorId() {
		TenantContext.setCurrentTenantId(2L);
		tenantHibernateFilterEnabler.enable(2L);

		assertThat(customerRepository.findById(1L)).isEmpty();
	}
}
