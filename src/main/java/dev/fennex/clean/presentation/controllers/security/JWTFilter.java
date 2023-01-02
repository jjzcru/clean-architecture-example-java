package dev.fennex.clean.presentation.controllers.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "180");
        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
            String jwt = authHeader.substring(7);

            if(jwt.isBlank()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
                return;
            }

            try{
                String userId = jwtUtil.validateTokenAndRetrieveSubject(jwt);
                CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                if(SecurityContextHolder.getContext().getAuthentication() == null){
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }catch(JWTVerificationException exc){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
