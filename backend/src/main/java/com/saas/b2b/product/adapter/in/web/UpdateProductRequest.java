package com.saas.b2b.product.adapter.in.web;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProductRequest(
		@NotBlank String name,
		@NotBlank String sku,
		@NotNull @DecimalMin("0.01") BigDecimal unitPrice) {
}
