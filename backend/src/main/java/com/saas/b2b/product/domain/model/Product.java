package com.saas.b2b.product.domain.model;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

	private final Long id;
	private final Long tenantId;
	private final String name;
	private final String sku;
	private final BigDecimal unitPrice;
	private final ProductStatus status;
}
