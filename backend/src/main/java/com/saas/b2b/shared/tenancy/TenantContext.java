package com.saas.b2b.shared.tenancy;

public final class TenantContext {

	private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

	private TenantContext() {
	}

	public static void setCurrentTenantId(Long tenantId) {
		CURRENT_TENANT.set(tenantId);
	}

	public static Long getCurrentTenantId() {
		return CURRENT_TENANT.get();
	}

	public static void clear() {
		CURRENT_TENANT.remove();
	}
}
