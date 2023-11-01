package com.app.service.tokens;

import com.app.model.dto.auth.AuthenticationDto;
import com.app.model.dto.auth.AuthorizationDto;
import com.app.model.dto.token.RefreshTokenDto;
import com.app.model.dto.token.TokensDto;

public interface TokensService {

    TokensDto generateToken(AuthenticationDto authenticationDto);
    AuthorizationDto parseTokens(String token);
    TokensDto refreshTokens(RefreshTokenDto refreshTokenDto);
}
