package com.saas.b2b.customer.application.port.out;

import java.util.List;
import java.util.Optional;

import com.saas.b2b.customer.domain.model.Customer;

public interface CustomerRepositoryPort {

	Optional<Customer> findById(Long id);

	List<Customer> findAllByTenantId(Long tenantId);

	Customer save(Customer customer);

	void deleteById(Long id);
}
