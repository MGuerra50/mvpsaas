package com.saas.b2b.product.application.port.in;

import com.saas.b2b.product.domain.model.ProductStatus;
import com.saas.b2b.shared.api.PageResult;
import com.saas.b2b.product.domain.model.Product;

public interface ListProductsUseCase {

	PageResult<Product> list(String search, ProductStatus status, int page, int size);
}
