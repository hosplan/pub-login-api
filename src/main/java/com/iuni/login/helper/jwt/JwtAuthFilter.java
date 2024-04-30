package com.iuni.login.helper.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthFilter extends OncePerRequestFilter {
    private final JWT jwt;
    public JwtAuthFilter(JWT jwt) {
        this.jwt = jwt;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwt.resolve(request);

        if(token != null && jwt.validate(token)) {
            //token = token.split(" ")[1].trim();
            Authentication auth = jwt.getAuth(token);
            System.out.println("auth = " + auth);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
