package com.app.service.authorization.impl;

import com.app.service.authorization.AuthorizationCheckService;
import com.app.service.authorization.exception.UriNotFoundException;
import com.app.service.authorization.security.AuthorizationRole;
import com.app.service.tokens.TokensService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.app.service.authorization.security.AuthorizationRole.*;

@Service
public class AuthorizationCheckServiceImpl implements AuthorizationCheckService {
    private final TokensService tokensService;
    private final Environment environment;
    private final EnumMap<AuthorizationRole, List<String>> authorizationUris;
    private final List<String> allUris;
    private static final Logger logger = LogManager.getLogger("DebugLogger");

    @Autowired
    public AuthorizationCheckServiceImpl(TokensService tokensService, Environment environment) {
        this.tokensService = tokensService;
        this.environment = environment;
        this.authorizationUris = initRoleUris();
        this.allUris = initAllUris();
    }

    private EnumMap<AuthorizationRole, List<String>> initRoleUris() {
        return new EnumMap<>(Map.of(
                USER,
                Arrays.stream(environment.getRequiredProperty("authorization.uris.USER").split(",")).toList(),
                ADMIN,
                Arrays.stream(environment.getRequiredProperty("authorization.uris.ADMIN").split(",")).toList()));
    }

    private List<String> initAllUris() {
        return Arrays
                .stream(environment.getRequiredProperty("authorization.uris.allUris").split(","))
                .toList();
    }

    private boolean containsURI(AuthorizationRole role, String uriToCheck) {
        return authorizationUris
                .get(role)
                .stream()
                .anyMatch(uri -> checkURI(uri, uriToCheck));
    }

    public boolean checkURI(String uri, String uriToCheck) {
        if (uri.endsWith("*")) {
            return uriToCheck.startsWith(uri.substring(0, uri.length() - 1));
        }
        return uriToCheck.equals(uri);
    }

    public boolean authorize(String accessToken, String uri) {
        if (!checkIfUriExist(uri)) {
            throw new UriNotFoundException("Uri not found");
        }

        if (accessToken == null) {
            logger.debug("Lack of accessToken. Checking all uris.");
            return allUris
                    .stream()
                    .anyMatch(u ->  checkURI(u,uri));
        }

        var role = toAuthorizationRole(tokensService
                .parseTokens(accessToken)
                .role());

        return containsURI(role, uri);
    }

    private boolean checkIfUriExist(String uriToCheck) {
        return Stream.of(allUris,
                authorizationUris.get(USER),
                authorizationUris
                .get(ADMIN)).flatMap(List::stream)
                        .anyMatch(uri -> checkURI(uri,uriToCheck));
    }
}
