package com.saas.b2b.shared.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtProperties jwtProperties;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.jwtProperties = jwtProperties;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Bearer ")) {
			String token = authorization.substring(7).trim();
			jwtTokenProvider.parseToken(token)
					.filter(jwtTokenProvider::isAccessToken)
					.ifPresent(this::authenticate);
		}
		filterChain.doFilter(request, response);
	}

	private void authenticate(Claims claims) {
		Long userId = jwtTokenProvider.extractUserId(claims).orElse(null);
		String email = (String) claims.get(jwtProperties.getEmailClaim());
		if (userId == null || email == null) {
			return;
		}
		var authentication = new UsernamePasswordAuthenticationToken(
				email,
				null,
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
