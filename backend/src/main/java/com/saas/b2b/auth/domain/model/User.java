package com.saas.b2b.auth.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

	private final Long id;
	private final Long tenantId;
	private final String name;
	private final String email;
	private final String passwordHash;
}
