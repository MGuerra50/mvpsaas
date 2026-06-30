package com.saas.b2b.tenant.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tenant {

	private final Long id;
	private final String name;
	private final TenantStatus status;
}
