package edu.pitt.api.Mongo.security;

import edu.pitt.api.Mongo.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Autowired
    private MyUserDetails myUserDetails;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000 * 12; // 1h


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Users user) {

        Claims claims = Jwts.claims().setSubject(user.getUsrname());
        claims.put("auth", user.getRoles().stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                .filter(Objects::nonNull).collect(Collectors.toList()));
        Date now = new Date();


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date.from(ZonedDateTime.now().plusHours(12).toInstant()))
//                .setExpiration(new Date(now.getTime() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }

}
