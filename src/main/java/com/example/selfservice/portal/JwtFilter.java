package com.example.selfservice.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private PortalService portalService;
    @Autowired
    private PortalRepository portalRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Inside filter method");
        String authorization = request.getHeader("Authorization");
        String token = null;
        String ID = null;
        String email = null;

        if(authorization != null && authorization.startsWith("Bearer")){
            token = authorization.substring(7);
            ID = jwtUtility.getIDFromToken(token);
            email = jwtUtility.getEmailFromToken(token);
            System.out.println(ID + " " + email);
        }

        if(ID != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails
                    = portalService.loadUserByUsername(email);

            if(jwtUtility.validateToken(token,userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        System.out.println("Reached this point within filter method");
        filterChain.doFilter(request, response);
    }
}
