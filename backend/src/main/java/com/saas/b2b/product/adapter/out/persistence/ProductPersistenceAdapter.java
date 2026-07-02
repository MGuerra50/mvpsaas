package com.saas.b2b.product.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.saas.b2b.product.application.port.out.ProductRepositoryPort;
import com.saas.b2b.product.domain.model.Product;
import com.saas.b2b.product.domain.model.ProductStatus;
import com.saas.b2b.shared.api.PageResult;

@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {

	private final ProductJpaRepository jpaRepository;

	public ProductPersistenceAdapter(ProductJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Product> findById(Long id) {
		return jpaRepository.findById(id).map(ProductPersistenceMapper::toDomain);
	}

	@Override
	public PageResult<Product> search(Long tenantId, String search, ProductStatus status, int page, int size) {
		var result = jpaRepository.search(
				tenantId,
				normalizeSearch(search),
				status,
				PageRequest.of(page, size));
		return new PageResult<>(
				result.map(ProductPersistenceMapper::toDomain).getContent(),
				result.getNumber(),
				result.getSize(),
				result.getTotalElements(),
				result.getTotalPages());
	}

	@Override
	public Product save(Product product) {
		ProductJpaEntity saved = jpaRepository.save(ProductPersistenceMapper.toEntity(product));
		return ProductPersistenceMapper.toDomain(saved);
	}

	private String normalizeSearch(String search) {
		return search == null ? "" : search.trim();
	}
}
