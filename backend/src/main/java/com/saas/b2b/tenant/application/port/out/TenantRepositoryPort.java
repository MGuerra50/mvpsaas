package com.saas.b2b.tenant.application.port.out;

import java.util.List;
import java.util.Optional;

import com.saas.b2b.tenant.domain.model.Tenant;

public interface TenantRepositoryPort {

	Optional<Tenant> findById(Long id);

	Tenant save(Tenant tenant);

	List<Tenant> findAll();
}
