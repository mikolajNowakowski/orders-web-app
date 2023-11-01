package com.app.service.tokens.impl;

import com.app.data.repository.user.UserRepository;
import com.app.model.dto.auth.AuthenticationDto;
import com.app.model.dto.auth.AuthorizationDto;
import com.app.model.dto.token.RefreshTokenDto;
import com.app.model.dto.token.TokensDto;
import com.app.model.user.User;
import com.app.service.tokens.TokensService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokensServiceImpl implements TokensService {

    @Value("${tokens.access.expiration_time_ms}")
    private Long accessTokenExpirationTimeMs;

    @Value("${tokens.refresh.expiration_time_ms}")
    private Long refreshTokenExpirationTimeMs;

    @Value("${tokens.refresh.access_token_expiration_time_ms_property}")
    private String refreshTokenProperty;
    // TODO o tym polu mówię
    @Value("${tokens.prefix}")
    private String tokensPrefix;
    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger("DebugLogger");

    @Override
    public TokensDto generateToken(AuthenticationDto authenticationDto) {
        var userFromDb = userRepository.findByUserName(authenticationDto.username())
                .orElseThrow(() -> {
                    logger.error("No user with inputted name.");
                    return new IllegalStateException("Authentication failed [1]!");
                });

        if(!userFromDb.isEnabled()){
            throw new IllegalStateException("To log in, user must confirm its email address.");
        }

        if (!passwordEncoder.matches(
                authenticationDto.password(),
                userFromDb.getPassword()
        )) {
            logger.error("Wrong password.");
            throw new IllegalStateException("Authentication failed [2]!");
        }

        var userId = userFromDb.getId();

        var currentDate = new Date();

        var accessTokenExpirationDate = new Date(currentDate.getTime() + accessTokenExpirationTimeMs);
        var refreshTokenExpirationDate = new Date(currentDate.getTime() + refreshTokenExpirationTimeMs);

        var accessToken = Jwts.builder()
                .setSubject(userId + "")
                .setExpiration(accessTokenExpirationDate)
                .setIssuedAt(currentDate)
                .signWith(secretKey)
                .compact();

        var refreshToken = Jwts.builder()
                .setSubject(userId + "")
                .setExpiration(refreshTokenExpirationDate)
                .setIssuedAt(currentDate)
                .claim(refreshTokenProperty, accessTokenExpirationDate.getTime())
                .signWith(secretKey)
                .compact();

        return new TokensDto(accessToken, refreshToken);
    }

    @Override
    public AuthorizationDto parseTokens(String token) {
        if (token == null) {
            throw new IllegalStateException("Token is null.");
        }

        if (!isTokenValid(token)) {
            throw new IllegalStateException("Token has been expired.");
        }

        var userId = id(token);

        return userRepository
                .findById(userId)
                .map(User::toAuthorizationDto)
                .orElseThrow(() -> {
                    logger.error("No user with id from token.");
                    return new IllegalStateException("Authorization failed.");
                });
    }

    @Override
    public TokensDto refreshTokens(RefreshTokenDto refreshTokenDto) {
        var token = refreshTokenDto.refreshToken();

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("JWT token is null or empty.");
        }

        if (!isTokenValid(token)) {
            throw new IllegalStateException("Refresh token has been expired.");
        }

        var accessTokenExpirationTimeMsFromRt = accessTokenExpirationDateMsInRefreshToken(token);

        if (accessTokenExpirationTimeMsFromRt < System.currentTimeMillis()) {
            throw new IllegalStateException(("Access token has been expired."));
        }

        var userId = id(token);

        var currentDate = new Date();

        var accessTokenExpirationDate = new Date(currentDate.getTime() + accessTokenExpirationTimeMs);
        var refreshTokenExpirationDate = expirationDate(token);

        var accessToken = Jwts.builder()
                .setSubject(userId + "")
                .setExpiration(accessTokenExpirationDate)
                .setIssuedAt(currentDate)
                .signWith(secretKey)
                .compact();

        var refreshToken = Jwts.builder()
                .setSubject(userId + "")
                .setExpiration(refreshTokenExpirationDate)
                .setIssuedAt(currentDate)
                .claim(refreshTokenProperty, accessTokenExpirationDate.getTime())
                .signWith(secretKey)
                .compact();

        return new TokensDto(accessToken, refreshToken);
    }

    private Claims claims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Long id(String token) {
        return Long.parseLong(
                claims(token)
                        .getSubject()
        );
    }

    private Date expirationDate(String token) {
        return claims(token).getExpiration();
    }

    private boolean isTokenValid(String token) {
        return expirationDate(token).after(new Date());
    }

    private Long accessTokenExpirationDateMsInRefreshToken(String token) {
        return claims(token).get(refreshTokenProperty, Long.class);
    }
}
