package com.saas.b2b.customer.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.saas.b2b.customer.application.port.out.CustomerRepositoryPort;
import com.saas.b2b.customer.domain.model.Customer;

@Component
public class CustomerPersistenceAdapter implements CustomerRepositoryPort {

	private final CustomerJpaRepository jpaRepository;

	public CustomerPersistenceAdapter(CustomerJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return jpaRepository.findById(id).map(CustomerPersistenceMapper::toDomain);
	}

	@Override
	public List<Customer> findAllByTenantId(Long tenantId) {
		return jpaRepository.findByTenantId(tenantId).stream()
				.map(CustomerPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public Customer save(Customer customer) {
		CustomerJpaEntity saved = jpaRepository.save(CustomerPersistenceMapper.toEntity(customer));
		return CustomerPersistenceMapper.toDomain(saved);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}
}
