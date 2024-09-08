package com.backend.dss.users.config.auth.filter;

import com.backend.dss.users.config.auth.TokenJwtConfig;
import com.backend.dss.users.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username;
        String password;

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException,
            ServletException {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = user.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(authorities)).add(
                "username", username).add("isAdmin", isAdmin).build();

        String jwt =
                Jwts.builder().subject(username).claims(claims).signWith(TokenJwtConfig.SECRET_KEY).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 3600000)).compact();

        response.addHeader(HttpHeaders.AUTHORIZATION, TokenJwtConfig.TOKEN_PREFIX + jwt);
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("token", jwt);
        body.put("message", "Login successful");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(TokenJwtConfig.CONTENT_TYPE_JSON);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Username or Password is incorrect");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(TokenJwtConfig.CONTENT_TYPE_JSON);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
