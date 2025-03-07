package com.travelBnb.config;

import com.travelBnb.entity.AppUserEntity;
import com.travelBnb.repository.AppUserRepository;
import com.travelBnb.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTReqeustFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private AppUserRepository appUserRepository;
    public JWTReqeustFilter(JwtService jwtService, AppUserRepository appUserRepository) {
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String tokenHeader = request.getHeader("Authorization");
            if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(8, tokenHeader.length() - 1);
                System.out.println(token);
                String username = jwtService.getUsername(token);
                Optional<AppUserEntity> opUsername = appUserRepository.findByUsername(username);
                if(opUsername.isPresent()){
                    AppUserEntity appUser = opUsername.get();
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(appUser,null, Collections.singleton(new SimpleGrantedAuthority(appUser.getRole())));
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }
            filterChain.doFilter(request,response);
    }
}
