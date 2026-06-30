package com.saas.b2b.customer.application.port.in;

import com.saas.b2b.shared.api.PageResult;
import com.saas.b2b.customer.domain.model.Customer;

public interface ListCustomersUseCase {

	PageResult<Customer> list(String search, int page, int size);
}
