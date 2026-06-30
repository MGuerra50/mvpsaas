package com.saas.b2b.customer.domain.exception;

import com.saas.b2b.shared.exception.ResourceNotFoundException;

public class CustomerNotFoundException extends ResourceNotFoundException {

	public CustomerNotFoundException(Long id) {
		super("Cliente não encontrado: " + id);
	}
}
