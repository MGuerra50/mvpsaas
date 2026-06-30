package com.saas.b2b.auth.adapter.out.persistence;

import com.saas.b2b.auth.domain.model.User;

public final class UserPersistenceMapper {

	private UserPersistenceMapper() {
	}

	public static User toDomain(UserJpaEntity entity) {
		return User.builder()
				.id(entity.getId())
				.tenantId(entity.getTenantId())
				.name(entity.getName())
				.email(entity.getEmail())
				.passwordHash(entity.getPasswordHash())
				.build();
	}

	public static UserJpaEntity toEntity(User user) {
		UserJpaEntity entity = new UserJpaEntity();
		entity.setId(user.getId());
		entity.setTenantId(user.getTenantId());
		entity.setName(user.getName());
		entity.setEmail(user.getEmail());
		entity.setPasswordHash(user.getPasswordHash());
		return entity;
	}
}
