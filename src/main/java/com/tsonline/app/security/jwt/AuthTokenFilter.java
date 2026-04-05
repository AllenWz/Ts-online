package com.tsonline.app.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());

		try {
			// 1) take jwt token from header
			String jwt = jwtUtils.getJwtFromHeader(request);
			// 2) validate token
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				// 3) take username from token
				String username = jwtUtils.getUsernameFromJwtToken(jwt);
				// 4) extract userdetails
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				// 5) Build auth token
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				// 6) Set detail info to auth token
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// 7) Set auth token to security context
				SecurityContextHolder.getContext().setAuthentication(authToken);
				logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
			}
		} catch (Exception ex) {
			logger.error("Cannot set user authentication: {}", ex);
		}
		filterChain.doFilter(request, response);
	}

}
