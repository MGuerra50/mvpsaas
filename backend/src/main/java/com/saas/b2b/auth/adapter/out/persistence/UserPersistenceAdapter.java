package com.saas.b2b.auth.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.saas.b2b.auth.application.port.out.UserRepositoryPort;
import com.saas.b2b.auth.domain.model.User;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

	private final UserJpaRepository jpaRepository;

	public UserPersistenceAdapter(UserJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<User> findById(Long id) {
		return jpaRepository.findById(id).map(UserPersistenceMapper::toDomain);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return jpaRepository.findByEmail(email).map(UserPersistenceMapper::toDomain);
	}

	@Override
	public User save(User user) {
		UserJpaEntity saved = jpaRepository.save(UserPersistenceMapper.toEntity(user));
		return UserPersistenceMapper.toDomain(saved);
	}
}
