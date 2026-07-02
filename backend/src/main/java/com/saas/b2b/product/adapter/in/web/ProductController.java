package com.saas.b2b.product.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saas.b2b.product.application.port.in.CreateProductUseCase;
import com.saas.b2b.product.application.port.in.GetProductUseCase;
import com.saas.b2b.product.application.port.in.ListProductsUseCase;
import com.saas.b2b.product.application.port.in.UpdateProductStatusUseCase;
import com.saas.b2b.product.application.port.in.UpdateProductUseCase;
import com.saas.b2b.product.domain.model.ProductStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ListProductsUseCase listProductsUseCase;
	private final GetProductUseCase getProductUseCase;
	private final CreateProductUseCase createProductUseCase;
	private final UpdateProductUseCase updateProductUseCase;
	private final UpdateProductStatusUseCase updateProductStatusUseCase;

	public ProductController(
			ListProductsUseCase listProductsUseCase,
			GetProductUseCase getProductUseCase,
			CreateProductUseCase createProductUseCase,
			UpdateProductUseCase updateProductUseCase,
			UpdateProductStatusUseCase updateProductStatusUseCase) {
		this.listProductsUseCase = listProductsUseCase;
		this.getProductUseCase = getProductUseCase;
		this.createProductUseCase = createProductUseCase;
		this.updateProductUseCase = updateProductUseCase;
		this.updateProductStatusUseCase = updateProductStatusUseCase;
	}

	@GetMapping
	ProductPageResponse list(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) ProductStatus status) {
		return ProductPageResponse.from(listProductsUseCase.list(search, status, page, size));
	}

	@GetMapping("/{id}")
	ProductResponse getById(@PathVariable Long id) {
		return ProductResponse.from(getProductUseCase.getById(id));
	}

	@PostMapping
	ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
		var product = createProductUseCase.create(new CreateProductUseCase.CreateProductCommand(
				request.name(), request.sku(), request.unitPrice()));
		return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(product));
	}

	@PutMapping("/{id}")
	ProductResponse update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
		var product = updateProductUseCase.update(id, new UpdateProductUseCase.UpdateProductCommand(
				request.name(), request.sku(), request.unitPrice()));
		return ProductResponse.from(product);
	}

	@PatchMapping("/{id}/status")
	ProductResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateProductStatusRequest request) {
		return ProductResponse.from(updateProductStatusUseCase.updateStatus(id, request.status()));
	}
}
