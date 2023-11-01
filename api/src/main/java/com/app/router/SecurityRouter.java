package com.app.router;

import com.app.dto.ResponseDto;
import com.app.model.dto.auth.AuthenticationDto;
import com.app.model.dto.token.RefreshTokenDto;
import com.app.service.authorization.AuthorizationCheckService;
import com.app.service.tokens.TokensService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spark.ResponseTransformer;


import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class SecurityRouter {

    private final TokensService tokensService;
    private final AuthorizationCheckService authorizationCheckService;
    private final ResponseTransformer responseTransformer;
    private final Gson gson;

    public void routes() {
        before((request, response) -> {
            var uriToCheck = request.uri();
            var token = request.cookie("accessToken");
            if (!authorizationCheckService.authorize(token, uriToCheck)) {
                halt(403, "Access denied!");
            }
        });

        path("/auth", () -> {
            post(
                    "/login",
                    ((request, response) -> {

                        var authenticationDto = gson.fromJson(request.body(), AuthenticationDto.class);

                        var tokens = tokensService.generateToken(authenticationDto);

                        response.header("Content-Type", "application/json;charset=utf-8");

                        response.cookie(
                                "/",
                                "accessToken",
                                tokens.accessToken(),
                                1000000,
                                false,
                                true);
                        response.cookie(
                                "/",
                                "refreshToken",
                                tokens.refreshToken(),
                                1000000,
                                false,
                                true);
                        return new ResponseDto<>(tokens);
                    }),
                    responseTransformer
            );

            post(
                    "/refresh",
                    ((request, response) -> {

                        var refreshTokenDto = gson.fromJson(request.body(), RefreshTokenDto.class);

                        var tokens = tokensService.refreshTokens(refreshTokenDto);

                        response.header("Content-Type", "application/json;charset=utf-8");

                        response.cookie(
                                "/",
                                "accessToken",
                                tokens.accessToken(),
                                1000000,
                                false,
                                true);
                        response.cookie(
                                "/",
                                "refreshToken",
                                tokens.refreshToken(),
                                1000000,
                                false,
                                true);

                        return new ResponseDto<>(tokens);
                    }),
                    responseTransformer
            );
        });
    }
}
