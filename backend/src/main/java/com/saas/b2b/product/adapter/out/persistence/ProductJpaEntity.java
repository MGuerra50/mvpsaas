package com.saas.b2b.product.adapter.out.persistence;

import java.math.BigDecimal;

import com.saas.b2b.product.domain.model.ProductStatus;
import com.saas.b2b.shared.tenancy.TenantAwareJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
		name = "products",
		uniqueConstraints = @UniqueConstraint(columnNames = { "tenant_id", "sku" }))
public class ProductJpaEntity extends TenantAwareJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String sku;

	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductStatus status;
}
