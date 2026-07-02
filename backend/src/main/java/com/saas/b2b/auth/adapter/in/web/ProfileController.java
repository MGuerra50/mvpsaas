package com.saas.b2b.auth.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saas.b2b.auth.application.port.in.ChangePasswordUseCase;
import com.saas.b2b.auth.application.port.in.GetProfileUseCase;
import com.saas.b2b.auth.application.port.in.UpdateProfileUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class ProfileController {

	private final GetProfileUseCase getProfileUseCase;
	private final UpdateProfileUseCase updateProfileUseCase;
	private final ChangePasswordUseCase changePasswordUseCase;

	public ProfileController(
			GetProfileUseCase getProfileUseCase,
			UpdateProfileUseCase updateProfileUseCase,
			ChangePasswordUseCase changePasswordUseCase) {
		this.getProfileUseCase = getProfileUseCase;
		this.updateProfileUseCase = updateProfileUseCase;
		this.changePasswordUseCase = changePasswordUseCase;
	}

	@GetMapping("/me")
	ProfileResponse getProfile() {
		return ProfileResponse.from(getProfileUseCase.getProfile());
	}

	@PutMapping("/profile")
	ProfileResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
		var profile = updateProfileUseCase.update(new UpdateProfileUseCase.UpdateProfileCommand(
				request.name(), request.profileImageUrl()));
		return ProfileResponse.from(profile);
	}

	@PutMapping("/password")
	ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		changePasswordUseCase.changePassword(new ChangePasswordUseCase.ChangePasswordCommand(
				request.currentPassword(), request.newPassword()));
		return ResponseEntity.noContent().build();
	}
}
