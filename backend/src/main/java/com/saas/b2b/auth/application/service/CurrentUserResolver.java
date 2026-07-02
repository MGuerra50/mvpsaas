package com.saas.b2b.auth.application.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.saas.b2b.auth.application.port.out.UserRepositoryPort;
import com.saas.b2b.auth.domain.exception.InvalidCurrentPasswordException;
import com.saas.b2b.auth.domain.model.User;
import com.saas.b2b.shared.exception.ResourceNotFoundException;
import com.saas.b2b.shared.tenancy.TenantContext;

@Component
public class CurrentUserResolver {

	private final UserRepositoryPort userRepositoryPort;

	public CurrentUserResolver(UserRepositoryPort userRepositoryPort) {
		this.userRepositoryPort = userRepositoryPort;
	}

	public User requireCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new ResourceNotFoundException("Usuário não autenticado.");
		}

		String email = authentication.getName();
		Long tenantId = TenantContext.requireCurrentTenantId();

		User user = userRepositoryPort.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

		if (!user.getTenantId().equals(tenantId)) {
			throw new ResourceNotFoundException("Usuário não encontrado.");
		}

		return user;
	}
}
