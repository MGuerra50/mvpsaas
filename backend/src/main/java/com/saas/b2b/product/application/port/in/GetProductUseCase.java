package com.saas.b2b.product.application.port.in;

import com.saas.b2b.product.domain.model.Product;

public interface GetProductUseCase {

	Product getById(Long id);
}
