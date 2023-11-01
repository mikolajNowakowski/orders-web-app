package com.app.router;

import com.app.dto.ResponseDto;
import com.app.service.authorization.exception.UriNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spark.ResponseTransformer;

import static spark.Spark.*;
import static spark.Spark.notFound;

@Component
@RequiredArgsConstructor
public class ErrorRouter {

    private final ResponseTransformer responseTransformer;
    public void routes() {

        exception(RuntimeException.class, (ex, request, response) -> {
            var exMsg = ex.getMessage();
            if (ex.getClass().equals(UriNotFoundException.class)) {
                response.status(404);
                response.body(ex.getMessage());
            } else {
                response.redirect("/error/" + exMsg, 301);
            }
        });

        path("/error", () -> {
            get(
                    "/:message",
                    (request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        response.status(500);
                        var message = request.params("message");
                        return new ResponseDto<>(message);
                    },
                    responseTransformer
            );
        });

        internalServerError((request, response) -> {
            response.header("Content-Type", "application/json;charset=utf-8");
            return new ResponseDto<>("Internal Server Error");
        });

        notFound((request, response) -> {
            response.header("Content-Type", "application/json;charset=utf-8");
            return new ResponseDto<>("Not found");
        });
    }
}
