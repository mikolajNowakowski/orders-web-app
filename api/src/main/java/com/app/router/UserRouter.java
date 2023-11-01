package com.app.router;

import com.app.dto.ResponseDto;
import com.app.model.dto.user.CreateUserDto;
import com.app.service.user.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spark.ResponseTransformer;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class UserRouter {
    private final UserService userService;
    private final ResponseTransformer responseTransformer;
    private final Gson gson;

    public void routes() {
        path("/users", () -> {
            post(
                    "",
                    (request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        var createUserDto = gson.fromJson(request.body(), CreateUserDto.class);
                        response.status(201);
                        return new ResponseDto<>(userService.register(createUserDto));
                    },
                    responseTransformer
            );

            get(
                    "/activate",
                    (request, response) -> {
                        var id = Long.parseLong(request.queryParams("id"));
                        var timestamp = Long.parseLong(request.queryParams("timestamp"));
                        response.header("Content-Type", "application/json;charset=utf-8");
                        response.status(200);
                        return new ResponseDto<>(userService.activate(id, timestamp));
                    },
                    responseTransformer
            );
        });
    }
}
