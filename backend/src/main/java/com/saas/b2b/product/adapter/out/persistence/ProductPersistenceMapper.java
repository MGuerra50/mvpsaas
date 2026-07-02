package com.saas.b2b.product.adapter.out.persistence;

import com.saas.b2b.product.domain.model.Product;

public final class ProductPersistenceMapper {

	private ProductPersistenceMapper() {
	}

	public static Product toDomain(ProductJpaEntity entity) {
		return Product.builder()
				.id(entity.getId())
				.tenantId(entity.getTenantId())
				.name(entity.getName())
				.sku(entity.getSku())
				.unitPrice(entity.getUnitPrice())
				.status(entity.getStatus())
				.build();
	}

	public static ProductJpaEntity toEntity(Product product) {
		ProductJpaEntity entity = new ProductJpaEntity();
		entity.setId(product.getId());
		entity.setTenantId(product.getTenantId());
		entity.setName(product.getName());
		entity.setSku(product.getSku());
		entity.setUnitPrice(product.getUnitPrice());
		entity.setStatus(product.getStatus());
		return entity;
	}
}
