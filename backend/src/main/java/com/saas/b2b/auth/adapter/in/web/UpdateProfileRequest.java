package com.saas.b2b.auth.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
		@NotBlank String name,
		@Size(max = 7_500_000) String profileImageUrl) {
}
