package com.saas.b2b.product.application.port.in;

import com.saas.b2b.product.domain.model.Product;
import com.saas.b2b.product.domain.model.ProductStatus;

public interface UpdateProductStatusUseCase {

	Product updateStatus(Long id, ProductStatus status);
}
