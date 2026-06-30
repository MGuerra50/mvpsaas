package com.saas.b2b.customer.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.saas.b2b.customer.application.port.out.CustomerRepositoryPort;
import com.saas.b2b.customer.domain.model.Customer;
import com.saas.b2b.shared.api.PageResult;

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
	public java.util.List<Customer> findAllByTenantId(Long tenantId) {
		return jpaRepository.search(tenantId, "", PageRequest.of(0, Integer.MAX_VALUE))
				.map(CustomerPersistenceMapper::toDomain)
				.getContent();
	}

	@Override
	public PageResult<Customer> search(Long tenantId, String search, int page, int size) {
		var result = jpaRepository.search(tenantId, normalizeSearch(search), PageRequest.of(page, size));
		return new PageResult<>(
				result.map(CustomerPersistenceMapper::toDomain).getContent(),
				result.getNumber(),
				result.getSize(),
				result.getTotalElements(),
				result.getTotalPages());
	}

	@Override
	public boolean existsById(Long id) {
		return jpaRepository.existsById(id);
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

	private String normalizeSearch(String search) {
		return search == null ? "" : search.trim();
	}
}
