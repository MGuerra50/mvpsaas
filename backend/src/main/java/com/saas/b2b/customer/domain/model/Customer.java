package com.saas.b2b.customer.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer {

	private final Long id;
	private final Long tenantId;
	private final String name;
	private final String phone;
	private final String documentId;
}
