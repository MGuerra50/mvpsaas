package com.saas.b2b.customer.adapter.in.web;

import com.saas.b2b.customer.domain.model.Customer;

public record CustomerResponse(
		Long id,
		Long tenantId,
		String name,
		String phone,
		String documentId) {

	public static CustomerResponse from(Customer customer) {
		return new CustomerResponse(
				customer.getId(),
				customer.getTenantId(),
				customer.getName(),
				customer.getPhone(),
				customer.getDocumentId());
	}
}
