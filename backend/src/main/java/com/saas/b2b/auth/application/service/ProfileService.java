package com.saas.b2b.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saas.b2b.auth.application.port.in.ChangePasswordUseCase;
import com.saas.b2b.auth.application.port.in.GetProfileUseCase;
import com.saas.b2b.auth.application.port.in.UpdateProfileUseCase;
import com.saas.b2b.auth.application.port.out.UserRepositoryPort;
import com.saas.b2b.auth.domain.exception.InvalidCurrentPasswordException;
import com.saas.b2b.auth.domain.model.User;

@Service
public class ProfileService implements GetProfileUseCase, UpdateProfileUseCase, ChangePasswordUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final CurrentUserResolver currentUserResolver;
	private final PasswordEncoder passwordEncoder;

	public ProfileService(
			UserRepositoryPort userRepositoryPort,
			CurrentUserResolver currentUserResolver,
			PasswordEncoder passwordEncoder) {
		this.userRepositoryPort = userRepositoryPort;
		this.currentUserResolver = currentUserResolver;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserProfile getProfile() {
		return UserProfile.from(currentUserResolver.requireCurrentUser());
	}

	@Override
	public UpdateProfileUseCase.UserProfile update(UpdateProfileCommand command) {
		User existing = currentUserResolver.requireCurrentUser();
		User updated = User.builder()
				.id(existing.getId())
				.tenantId(existing.getTenantId())
				.name(command.name())
				.email(existing.getEmail())
				.passwordHash(existing.getPasswordHash())
				.profileImageUrl(command.profileImageUrl())
				.build();
		return UpdateProfileUseCase.UserProfile.from(userRepositoryPort.save(updated));
	}

	@Override
	public void changePassword(ChangePasswordCommand command) {
		User existing = currentUserResolver.requireCurrentUser();
		if (!passwordEncoder.matches(command.currentPassword(), existing.getPasswordHash())) {
			throw new InvalidCurrentPasswordException();
		}

		User updated = User.builder()
				.id(existing.getId())
				.tenantId(existing.getTenantId())
				.name(existing.getName())
				.email(existing.getEmail())
				.passwordHash(passwordEncoder.encode(command.newPassword()))
				.profileImageUrl(existing.getProfileImageUrl())
				.build();
		userRepositoryPort.save(updated);
	}
}
