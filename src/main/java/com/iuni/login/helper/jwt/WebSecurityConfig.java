package com.iuni.login.helper.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final JWT jwt;
    private ObjectMapper objectMapper = new ObjectMapper();
    public WebSecurityConfig(JWT jwt) {
        this.jwt = jwt;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("api/member", "api/signIn", "api/iunitoken", "api/signIn/**").permitAll()
                                .requestMatchers("api/membermap","api/membermap/**", "api/alarm").hasAuthority("USER")
                                .requestMatchers("api/iuniStyle","api/iuniStyle/**", "api/member/**", "api/alarm/load").hasAnyAuthority("USER","TEMPUSER")
                                .anyRequest().denyAll()
                )
                .addFilterBefore(new JwtAuthFilter(jwt), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e ->
                        e
                        .accessDeniedHandler(new AccessDeniedHandler() {
                                    @Override
                                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
                                        response.setStatus(403);
                                        response.setCharacterEncoding("utf-8");
                                        response.setContentType("text/html; charset=utf-8");
                                        HashMap<String, String> resultData = new HashMap<>();
                                        resultData.put("token_error", "noAuth");
                                        String result = objectMapper.writeValueAsString(resultData);
                                        response.getWriter().write(result);
                                    }
                        })
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
                                response.setStatus(401);
                                response.setCharacterEncoding("utf-8");
                                response.setContentType("text/html; charset=utf-8");
                                HashMap<String, String> resultData = new HashMap<>();
                                resultData.put("token_error", "noToken");
                                String result = objectMapper.writeValueAsString(resultData);
                                response.getWriter().write(result);
                            }
                        })
                );

        return httpSecurity.build();
    }
}
