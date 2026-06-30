package com.saas.b2b.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
		@NotBlank String name,
		@NotBlank String phone,
		@NotBlank String documentId) {
}
