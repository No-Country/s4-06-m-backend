package ecommerce.eco.sport.config.filters;

import ecommerce.eco.sport.config.utils.JwtUtil;
import ecommerce.eco.sport.config.utils.ResponseUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String CREDENTIALS = "";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(jwtUtil.isValidToken(authorizationHeader)){
            try {
                setAuthentication(authorizationHeader);
                filterChain.doFilter(request, response);
            }catch (JwtException e){
                ResponseUtils.setCustomForbiddenResponse(response);
            }
        }else {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }

    private void setAuthentication(String authorizationHeader) {
        List<GrantedAuthority> authorities = jwtUtil.getAuthorities(authorizationHeader);
        if (authorities == null || authorities.isEmpty()) {
            throw new IllegalArgumentException("User must have one authority.");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                jwtUtil.getUsernameFromToken(authorizationHeader),
                CREDENTIALS,
                authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
