package com.saas.b2b.auth.application.port.in;

public interface ChangePasswordUseCase {

	void changePassword(ChangePasswordCommand command);

	record ChangePasswordCommand(String currentPassword, String newPassword) {
	}
}
