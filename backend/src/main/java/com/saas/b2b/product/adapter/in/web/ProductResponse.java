package com.saas.b2b.product.adapter.in.web;

import java.math.BigDecimal;

import com.saas.b2b.product.domain.model.Product;

public record ProductResponse(
		Long id,
		Long tenantId,
		String name,
		String sku,
		BigDecimal unitPrice,
		String status) {

	public static ProductResponse from(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getTenantId(),
				product.getName(),
				product.getSku(),
				product.getUnitPrice(),
				product.getStatus().name());
	}
}
