package com.saas.b2b.customer.application.port.in;

import com.saas.b2b.customer.domain.model.Customer;

public interface UpdateCustomerUseCase {

	Customer update(Long id, UpdateCustomerCommand command);

	record UpdateCustomerCommand(String name, String phone, String documentId) {
	}
}
