package com.saas.b2b.product.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.saas.b2b.product.domain.model.ProductStatus;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

	@Query("""
			SELECT p FROM ProductJpaEntity p
			WHERE p.tenantId = :tenantId
			AND (
				:search IS NULL OR :search = ''
				OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
				OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%'))
			)
			AND (:status IS NULL OR p.status = :status)
			ORDER BY p.name ASC
			""")
	Page<ProductJpaEntity> search(
			@Param("tenantId") Long tenantId,
			@Param("search") String search,
			@Param("status") ProductStatus status,
			Pageable pageable);
}
