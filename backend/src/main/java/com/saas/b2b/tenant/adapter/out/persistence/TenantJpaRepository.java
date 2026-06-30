package com.saas.b2b.tenant.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantJpaRepository extends JpaRepository<TenantJpaEntity, Long> {
}
