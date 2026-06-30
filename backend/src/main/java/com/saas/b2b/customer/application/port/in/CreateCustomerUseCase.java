package com.saas.b2b.customer.application.port.in;

import com.saas.b2b.customer.domain.model.Customer;

public interface CreateCustomerUseCase {

	Customer create(CreateCustomerCommand command);

	record CreateCustomerCommand(String name, String phone, String documentId) {
	}
}
