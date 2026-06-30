package com.saas.b2b.shared.tenancy;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saas.b2b.shared.security.JwtTenantExtractor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantContextFilter extends OncePerRequestFilter {

	private final JwtTenantExtractor jwtTenantExtractor;
	private final TenantHibernateFilterEnabler tenantHibernateFilterEnabler;

	public TenantContextFilter(
			JwtTenantExtractor jwtTenantExtractor,
			TenantHibernateFilterEnabler tenantHibernateFilterEnabler) {
		this.jwtTenantExtractor = jwtTenantExtractor;
		this.tenantHibernateFilterEnabler = tenantHibernateFilterEnabler;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			jwtTenantExtractor.extractTenantId(request.getHeader("Authorization"))
					.ifPresent(tenantId -> {
						TenantContext.setCurrentTenantId(tenantId);
						tenantHibernateFilterEnabler.enable(tenantId);
					});
			filterChain.doFilter(request, response);
		}
		finally {
			TenantContext.clear();
		}
	}
}
