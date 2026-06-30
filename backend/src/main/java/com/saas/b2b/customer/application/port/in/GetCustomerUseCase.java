package com.saas.b2b.customer.application.port.in;

import com.saas.b2b.customer.domain.model.Customer;

public interface GetCustomerUseCase {

	Customer getById(Long id);
}
