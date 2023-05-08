package uz.optimit.taxi.configuration.jwtConfig;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.exception.RefreshTokeNotFound;
import uz.optimit.taxi.exception.TimeExceededException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.repository.UserRepository;

import java.util.Date;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class JwtGenerate {
    private final UserRepository userRepository;

    private static final String JWT_ACCESS_KEY = "404E635266556A586E327235753878F413F4428472B4B6250645367566B5970";
    private static final String JWT_REFRESH_KEY = "404E635266556A586E327235753878F413F4428472B4B6250645lll367566B5970";
    private static final long accessTokenLiveTime = 10 * 60 * 100000;
    private static final long reFreshTokenLiveTime = 1_00000 * 60 * 60 * 24;

    public static synchronized String generateAccessToken(
            User user
    ) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JWT_ACCESS_KEY)
                .setSubject(user.getPhone())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessTokenLiveTime))
                .claim(AUTHORITIES, user.getAuthorities())
                .compact();
    }

    public static synchronized String generateRefreshToken(User user) {
        return REFRESH_TOKEN + Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JWT_REFRESH_KEY)
                .setSubject(user.getPhone())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + reFreshTokenLiveTime))
                .compact();
    }

    public static synchronized Claims isValidAccessToken(String token) {
        return getAccessClaim(token);
    }

    public static synchronized Claims isValidRefreshToken(String token) {
        return getRefreshClaim(token);
    }

    public String checkRefreshTokenValidAndGetAccessToken(HttpServletRequest request) throws Exception {
        String requestHeader = request.getHeader(AUTHORIZATION);
        if (requestHeader == null || !requestHeader.startsWith(REFRESH_TOKEN)) {
            throw new RefreshTokeNotFound(REFRESH_TOKEN_NOT_FOUND);
        }
        String token = requestHeader.replace(REFRESH_TOKEN, "");
        Claims claims = JwtGenerate.isValidRefreshToken(token);
        if (claims == null) {
            throw new Exception();
        }
        User user = userRepository.findByPhone(claims.getSubject()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return JwtGenerate.generateAccessToken(user);
    }

    private static synchronized Claims getAccessClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_ACCESS_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException | SignatureException | UnsupportedJwtException | MalformedJwtException |
                 IllegalArgumentException e) {
            throw new TimeExceededException(REFRESH_TOKEN_TIME_OUT);
        }
    }

    public static synchronized boolean isValid(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(JWT_ACCESS_KEY).parseClaimsJws(token).getBody();
            if (body != null)
                return true;
        } catch (ExpiredJwtException | SignatureException | UnsupportedJwtException | MalformedJwtException |
                 IllegalArgumentException e) {
            throw e;
        }
        return false;
    }

    private static synchronized Claims getRefreshClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_REFRESH_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException | SignatureException | UnsupportedJwtException | MalformedJwtException |
                 IllegalArgumentException e) {
            throw new TimeExceededException(REFRESH_TOKEN_TIME_OUT);
        }
    }
}
