package com.saas.b2b.shared.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.saas.b2b.auth.application.port.out.UserRepositoryPort;
import com.saas.b2b.auth.domain.model.User;

@Component
public class SeedPasswordFixRunner implements ApplicationRunner {

	private static final String DEMO_PASSWORD = "password";

	private final UserRepositoryPort userRepository;
	private final PasswordEncoder passwordEncoder;

	public SeedPasswordFixRunner(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) {
		fixPasswordIfNeeded("admin@alpha.com");
		fixPasswordIfNeeded("admin@beta.com");
	}

	private void fixPasswordIfNeeded(String email) {
		userRepository.findByEmail(email).ifPresent(user -> {
			if (passwordEncoder.matches(DEMO_PASSWORD, user.getPasswordHash())) {
				return;
			}
			userRepository.save(User.builder()
					.id(user.getId())
					.tenantId(user.getTenantId())
					.name(user.getName())
					.email(user.getEmail())
					.passwordHash(passwordEncoder.encode(DEMO_PASSWORD))
					.profileImageUrl(user.getProfileImageUrl())
					.build());
		});
	}
}
