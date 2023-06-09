package cart.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secret;
    @Value("${security.jwt.token.expire-length}")
    private Long validityInMilliseconds;
    @Value("${security.jwt.token.server-info}")
    private String serverInfo;

    public String createAccessToken(final String payload) {
        final Claims claims = generateClaims(payload);
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    private Claims generateClaims(final String payload) {
        return Jwts.claims().setSubject(payload);
    }

    public String createRefreshToken() {
        final Claims claims = generateClaims(serverInfo);
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    public String getClaims(final String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateAccessToken(final String token) {
        try {
            final Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(final String token) {
        try {
            final Claims claimsBody = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

            return claimsBody.getExpiration().after(new Date()) && claimsBody.getSubject().equals(serverInfo);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
