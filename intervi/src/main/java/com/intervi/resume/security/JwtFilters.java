package com.intervi.resume.security;

import com.intervi.resume.repository.BlackListedTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilters extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BlackListedTokenRepository blackListedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // skip filter for auth routes
        if (path.startsWith("/api/auth/register") ||
                path.startsWith("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("No token provided");
            return;
        }

        String token = authHeader.substring(7);

        if (blackListedTokenRepository.existsByToken(token)) {
            response.setStatus(401);
            response.getWriter().write("Token is invalid, please login again");
            return;
        }

        if (!jwtUtil.isTokenValid(token)) {
            response.setStatus(401);
            response.getWriter().write("Token is expired or invalid");
            return;
        }

        String email = jwtUtil.getEmailFromToken(token);
        request.setAttribute("email", email);

        filterChain.doFilter(request, response);
    }
}
