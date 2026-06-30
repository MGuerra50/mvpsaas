package com.saas.b2b.tenant.adapter.out.persistence;

import com.saas.b2b.tenant.domain.model.Tenant;

public final class TenantPersistenceMapper {

	private TenantPersistenceMapper() {
	}

	public static Tenant toDomain(TenantJpaEntity entity) {
		return Tenant.builder()
				.id(entity.getId())
				.name(entity.getName())
				.status(entity.getStatus())
				.build();
	}

	public static TenantJpaEntity toEntity(Tenant tenant) {
		TenantJpaEntity entity = new TenantJpaEntity();
		entity.setId(tenant.getId());
		entity.setName(tenant.getName());
		entity.setStatus(tenant.getStatus());
		return entity;
	}
}
