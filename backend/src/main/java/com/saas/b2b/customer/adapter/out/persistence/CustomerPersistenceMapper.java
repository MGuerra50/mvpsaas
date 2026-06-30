package com.saas.b2b.customer.adapter.out.persistence;

import com.saas.b2b.customer.domain.model.Customer;

public final class CustomerPersistenceMapper {

	private CustomerPersistenceMapper() {
	}

	public static Customer toDomain(CustomerJpaEntity entity) {
		return Customer.builder()
				.id(entity.getId())
				.tenantId(entity.getTenantId())
				.name(entity.getName())
				.phone(entity.getPhone())
				.documentId(entity.getDocumentId())
				.build();
	}

	public static CustomerJpaEntity toEntity(Customer customer) {
		CustomerJpaEntity entity = new CustomerJpaEntity();
		entity.setId(customer.getId());
		entity.setTenantId(customer.getTenantId());
		entity.setName(customer.getName());
		entity.setPhone(customer.getPhone());
		entity.setDocumentId(customer.getDocumentId());
		return entity;
	}
}
