package com.saas.b2b.product.application.port.in;

import java.math.BigDecimal;

import com.saas.b2b.product.domain.model.Product;

public interface UpdateProductUseCase {

	Product update(Long id, UpdateProductCommand command);

	record UpdateProductCommand(String name, String sku, BigDecimal unitPrice) {
	}
}
