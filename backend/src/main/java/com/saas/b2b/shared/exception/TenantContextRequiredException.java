package com.saas.b2b.shared.exception;

public class TenantContextRequiredException extends RuntimeException {

	public TenantContextRequiredException() {
		super("Contexto de tenant não disponível");
	}
}
