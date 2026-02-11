package com.example.demoDaoSecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter
{
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // token validate
        String BearerToken = request.getHeader("Authorization");
        String token = null;
        System.out.println(BearerToken);
        if(BearerToken != null && BearerToken.startsWith("Bearer "))
        {
            token = BearerToken.substring(7);

            if(jwtUtil.validate(token))
            {
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                System.out.println(username + " - "+ role);
                List<SimpleGrantedAuthority> roles = List.of(role).stream()
                        .map(x -> new SimpleGrantedAuthority(x))
                        .toList();

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        else
        {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        }
        filterChain.doFilter(request, response);
    }
}