package com.saas.b2b.customer.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, Long> {

	@Query("""
			SELECT c FROM CustomerJpaEntity c
			WHERE c.tenantId = :tenantId
			AND (
				:search IS NULL OR :search = ''
				OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
				OR c.phone LIKE CONCAT('%', :search, '%')
				OR c.documentId LIKE CONCAT('%', :search, '%')
			)
			""")
	Page<CustomerJpaEntity> search(
			@Param("tenantId") Long tenantId,
			@Param("search") String search,
			Pageable pageable);
}
