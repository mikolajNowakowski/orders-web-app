package com.app.service.authorization;


public interface AuthorizationCheckService {
    boolean authorize(String accessToken,String uri);
}
