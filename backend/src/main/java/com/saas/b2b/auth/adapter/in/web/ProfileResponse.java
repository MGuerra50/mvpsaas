package com.saas.b2b.auth.adapter.in.web;

import com.saas.b2b.auth.application.port.in.GetProfileUseCase;
import com.saas.b2b.auth.application.port.in.UpdateProfileUseCase;

public record ProfileResponse(
		Long id,
		Long tenantId,
		String name,
		String email,
		String profileImageUrl) {

	public static ProfileResponse from(GetProfileUseCase.UserProfile profile) {
		return new ProfileResponse(
				profile.id(),
				profile.tenantId(),
				profile.name(),
				profile.email(),
				profile.profileImageUrl());
	}

	public static ProfileResponse from(UpdateProfileUseCase.UserProfile profile) {
		return new ProfileResponse(
				profile.id(),
				profile.tenantId(),
				profile.name(),
				profile.email(),
				profile.profileImageUrl());
	}
}
