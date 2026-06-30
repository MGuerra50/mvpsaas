package com.saas.b2b.shared.tenancy;

import com.saas.b2b.shared.exception.TenantContextRequiredException;

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

	public static Long requireCurrentTenantId() {
		Long tenantId = CURRENT_TENANT.get();
		if (tenantId == null) {
			throw new TenantContextRequiredException();
		}
		return tenantId;
	}

	public static void clear() {
		CURRENT_TENANT.remove();
	}
}
