package org.effectivemobile.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.effectivemobile.services.BankAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.effectivemobile.services.ClientDetailsServiceImpl;
import java.io.IOException;
import java.security.SignatureException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ClientDetailsServiceImpl clientDetailsService;
    private static final Logger logger = LogManager.getLogger(JwtRequestFilter.class);
    @Override
    protected void doFilterInternal(@NonNull jakarta.servlet.http.HttpServletRequest request,
                                    @NonNull jakarta.servlet.http.HttpServletResponse response,
                                    @NonNull jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        try {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
            jwt = authHeader.substring(7);
            username = jwtService.extractUserEmail(jwt);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    ClientDetails clientDetails = (ClientDetails) this.clientDetailsService.loadUserByUsername(username);
                    if(jwtService.isTokenValid(jwt, clientDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                clientDetails,
                                null,
                                clientDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
            }
        }catch (ExpiredJwtException e) {
            logger.debug("The token's lifetime is up");
        }catch (UsernameNotFoundException e) {
            logger.debug("There's no user with that e-mail address");
        } catch (HttpMessageNotReadableException e) {
            logger.debug("Bad request");
        }
        filterChain.doFilter(request, response);
    }
}
