package com.saas.b2b.customer.application.port.out;

import java.util.List;
import java.util.Optional;

import com.saas.b2b.customer.domain.model.Customer;
import com.saas.b2b.shared.api.PageResult;

public interface CustomerRepositoryPort {

	Optional<Customer> findById(Long id);

	List<Customer> findAllByTenantId(Long tenantId);

	PageResult<Customer> search(Long tenantId, String search, int page, int size);

	boolean existsById(Long id);

	Customer save(Customer customer);

	void deleteById(Long id);
}
