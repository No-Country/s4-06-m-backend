package ecommerce.eco.sport.config.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtUtil {

    private static final String SECRET_KEY = "SECRET";
    private static final String AUTHORITIES = "authorities";
    private static final String BEARER_TOKEN = "Bearer %s";
    private static final String BEARER_PART = "Bearer ";
    private static final String EMPTY = "";

    public boolean isValidToken(String token) {
        //Comprueba si la cadena empieza por el prefijo pasado como parámetro.
        return token != null && token.startsWith(BEARER_PART);
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(
                Objects.toString(getAllClaimsFromToken(token).get(AUTHORITIES)));
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(getToken(token))
                .getBody();
    }
    private String getToken(String token) {
        return token.replace(BEARER_PART, EMPTY);
    }

    public Object getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver ) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        String token = Jwts.builder()
                .claim(AUTHORITIES, getFirstAuthority(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(40).toInstant()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
        return String.format(BEARER_TOKEN, token);
    }

    private String getFirstAuthority(UserDetails userDetails) {
        Optional<? extends GrantedAuthority> authority = userDetails.getAuthorities()
                .stream()
                .findFirst();

        if (authority.isEmpty()) {
            throw new IllegalArgumentException("User must have one authority.");
        }

        return authority.get().getAuthority();
    }
}
