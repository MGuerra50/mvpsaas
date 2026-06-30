package com.saas.b2b.customer.adapter.in.web;

import java.util.List;

import com.saas.b2b.customer.domain.model.Customer;
import com.saas.b2b.shared.api.PageResult;

public record CustomerPageResponse(
		List<CustomerResponse> content,
		int page,
		int size,
		long totalElements,
		int totalPages) {

	public static CustomerPageResponse from(PageResult<Customer> pageResult) {
		return new CustomerPageResponse(
				pageResult.content().stream().map(CustomerResponse::from).toList(),
				pageResult.page(),
				pageResult.size(),
				pageResult.totalElements(),
				pageResult.totalPages());
	}
}
