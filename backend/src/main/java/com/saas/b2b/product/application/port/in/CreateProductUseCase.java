package com.saas.b2b.product.application.port.in;

import java.math.BigDecimal;

import com.saas.b2b.product.domain.model.Product;

public interface CreateProductUseCase {

	Product create(CreateProductCommand command);

	record CreateProductCommand(String name, String sku, BigDecimal unitPrice) {
	}
}
