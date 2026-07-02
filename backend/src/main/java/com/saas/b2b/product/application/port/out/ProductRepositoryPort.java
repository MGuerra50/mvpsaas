package com.saas.b2b.product.application.port.out;

import java.util.Optional;

import com.saas.b2b.product.domain.model.Product;
import com.saas.b2b.product.domain.model.ProductStatus;
import com.saas.b2b.shared.api.PageResult;

public interface ProductRepositoryPort {

	Optional<Product> findById(Long id);

	PageResult<Product> search(Long tenantId, String search, ProductStatus status, int page, int size);

	Product save(Product product);
}
