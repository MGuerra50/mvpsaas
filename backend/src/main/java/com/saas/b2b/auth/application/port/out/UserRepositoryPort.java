package com.saas.b2b.auth.application.port.out;

import java.util.Optional;

import com.saas.b2b.auth.domain.model.User;

public interface UserRepositoryPort {

	Optional<User> findById(Long id);

	Optional<User> findByEmail(String email);

	User save(User user);
}
