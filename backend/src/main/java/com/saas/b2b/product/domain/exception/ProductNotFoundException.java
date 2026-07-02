package com.saas.b2b.product.domain.exception;

import com.saas.b2b.shared.exception.ResourceNotFoundException;

public class ProductNotFoundException extends ResourceNotFoundException {

	public ProductNotFoundException(Long id) {
		super("Produto não encontrado: " + id);
	}
}
