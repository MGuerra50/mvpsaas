package com.saas.b2b.product.application.service;

import org.springframework.stereotype.Service;

import com.saas.b2b.product.application.port.in.CreateProductUseCase;
import com.saas.b2b.product.application.port.in.GetProductUseCase;
import com.saas.b2b.product.application.port.in.ListProductsUseCase;
import com.saas.b2b.product.application.port.in.UpdateProductStatusUseCase;
import com.saas.b2b.product.application.port.in.UpdateProductUseCase;
import com.saas.b2b.product.application.port.out.ProductRepositoryPort;
import com.saas.b2b.product.domain.exception.ProductNotFoundException;
import com.saas.b2b.product.domain.model.Product;
import com.saas.b2b.product.domain.model.ProductStatus;
import com.saas.b2b.shared.api.PageResult;
import com.saas.b2b.shared.tenancy.TenantContext;

@Service
public class ProductService implements
		ListProductsUseCase,
		GetProductUseCase,
		CreateProductUseCase,
		UpdateProductUseCase,
		UpdateProductStatusUseCase {

	private final ProductRepositoryPort productRepositoryPort;

	public ProductService(ProductRepositoryPort productRepositoryPort) {
		this.productRepositoryPort = productRepositoryPort;
	}

	@Override
	public PageResult<Product> list(String search, ProductStatus status, int page, int size) {
		Long tenantId = TenantContext.requireCurrentTenantId();
		return productRepositoryPort.search(tenantId, search, status, page, size);
	}

	@Override
	public Product getById(Long id) {
		return productRepositoryPort.findById(id)
				.orElseThrow(() -> new ProductNotFoundException(id));
	}

	@Override
	public Product create(CreateProductCommand command) {
		Long tenantId = TenantContext.requireCurrentTenantId();
		Product product = Product.builder()
				.tenantId(tenantId)
				.name(command.name())
				.sku(command.sku())
				.unitPrice(command.unitPrice())
				.status(ProductStatus.ACTIVE)
				.build();
		return productRepositoryPort.save(product);
	}

	@Override
	public Product update(Long id, UpdateProductCommand command) {
		Product existing = getById(id);
		Product updated = Product.builder()
				.id(existing.getId())
				.tenantId(existing.getTenantId())
				.name(command.name())
				.sku(command.sku())
				.unitPrice(command.unitPrice())
				.status(existing.getStatus())
				.build();
		return productRepositoryPort.save(updated);
	}

	@Override
	public Product updateStatus(Long id, ProductStatus status) {
		Product existing = getById(id);
		Product updated = Product.builder()
				.id(existing.getId())
				.tenantId(existing.getTenantId())
				.name(existing.getName())
				.sku(existing.getSku())
				.unitPrice(existing.getUnitPrice())
				.status(status)
				.build();
		return productRepositoryPort.save(updated);
	}
}
