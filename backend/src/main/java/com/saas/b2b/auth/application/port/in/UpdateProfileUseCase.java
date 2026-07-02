package com.saas.b2b.auth.application.port.in;

import com.saas.b2b.auth.domain.model.User;

public interface UpdateProfileUseCase {

	UserProfile update(UpdateProfileCommand command);

	record UpdateProfileCommand(String name, String profileImageUrl) {
	}

	record UserProfile(Long id, Long tenantId, String name, String email, String profileImageUrl) {
		public static UserProfile from(User user) {
			return new UserProfile(
					user.getId(),
					user.getTenantId(),
					user.getName(),
					user.getEmail(),
					user.getProfileImageUrl());
		}
	}
}
