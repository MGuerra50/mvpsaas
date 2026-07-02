package com.saas.b2b.product.adapter.in.web;

import com.saas.b2b.product.domain.model.ProductStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateProductStatusRequest(@NotNull ProductStatus status) {
}
