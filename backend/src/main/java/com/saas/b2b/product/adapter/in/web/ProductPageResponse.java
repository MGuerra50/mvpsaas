package com.saas.b2b.product.adapter.in.web;

import com.saas.b2b.shared.api.PageResult;
import com.saas.b2b.product.domain.model.Product;

public record ProductPageResponse(
		java.util.List<ProductResponse> content,
		int page,
		int size,
		long totalElements,
		int totalPages) {

	public static ProductPageResponse from(PageResult<Product> pageResult) {
		return new ProductPageResponse(
				pageResult.content().stream().map(ProductResponse::from).toList(),
				pageResult.page(),
				pageResult.size(),
				pageResult.totalElements(),
				pageResult.totalPages());
	}
}
