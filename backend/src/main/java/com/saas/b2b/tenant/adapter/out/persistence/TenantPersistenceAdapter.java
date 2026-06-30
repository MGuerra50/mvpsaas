package com.saas.b2b.tenant.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.saas.b2b.tenant.application.port.out.TenantRepositoryPort;
import com.saas.b2b.tenant.domain.model.Tenant;

@Component
public class TenantPersistenceAdapter implements TenantRepositoryPort {

	private final TenantJpaRepository jpaRepository;

	public TenantPersistenceAdapter(TenantJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Tenant> findById(Long id) {
		return jpaRepository.findById(id).map(TenantPersistenceMapper::toDomain);
	}

	@Override
	public Tenant save(Tenant tenant) {
		TenantJpaEntity saved = jpaRepository.save(TenantPersistenceMapper.toEntity(tenant));
		return TenantPersistenceMapper.toDomain(saved);
	}

	@Override
	public List<Tenant> findAll() {
		return jpaRepository.findAll().stream()
				.map(TenantPersistenceMapper::toDomain)
				.toList();
	}
}
