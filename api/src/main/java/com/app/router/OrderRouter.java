package com.app.router;

import com.app.dto.ResponseDto;
import com.app.model.dto.order.OrderDto;
import com.app.service.order.OrderService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spark.ResponseTransformer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.app.model.comparing_type.ComparingType.*;
import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class OrderRouter {
    private final OrderService orderService;
    private final ResponseTransformer responseTransformer;
    private final Gson gson;

    public void routes() {
        path("/admin", () -> {

            // POST http://localhost:8080/admin/order
            post(
                    "/order",
                    (request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        JsonObject json = JsonParser.parseString(request.body()).getAsJsonObject();
                        var orderDto = gson.fromJson(request.body(), OrderDto.class);
                        return new ResponseDto<>(orderService.save(orderDto));
                    },
                    responseTransformer
            );
            // GET http://localhost:8080/admin/prices/discount
            get(
                    "/prices/discount",
                    (request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(orderService.getPricesOfAllOrdersWithDiscount());
                    },
                    responseTransformer
            );

            path("/customers", () -> {
                // GET  http://localhost:8080/admin/customers/top-paying
                get(
                        "/top-paying",
                        (request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(orderService.getCustomersWhoPayTheMost());
                        },
                        responseTransformer
                );

                // GET  http://localhost:8080/admin/customers/analyze-purchases/:n/:filepath
                get(
                        "/analyze-purchases/:n/:filepath",
                        (request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            int n = Integer.parseInt(request.params("n"));
                            String filepath = request.params("filepath");
                            return new ResponseDto<>(orderService.numberOfCustomersWhoBoughtTheSameProductSpecifiedTimesAndWriteToJson(n, filepath));
                        },
                        responseTransformer
                );
            });

            path("/months", () -> {
                // GET  http://localhost:8080/orders/months/products
                get(
                        "/products",
                        (request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(orderService.monthsWithNumberOfOrderedProducts());
                        },
                        responseTransformer
                );
                // GET  http://localhost:8080/orders/months/category
                get(
                        "/category",
                        (request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(orderService.monthsWithTheMostPopularCategory());
                        },
                        responseTransformer
                );
            });

            // GET  http://localhost:8080/admin/date/min-purchases
            get(
                    "/date/min-purchases",
                    (request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(orderService.getDateWithMinMaxPurchases(MIN));
                    },
                    responseTransformer
            );

            // GET  http://localhost:8080/admin/date/max-purchases
            get(
                    "/date/max-purchases",
                    (request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(orderService.getDateWithMinMaxPurchases(MAX));
                    },
                    responseTransformer
            );
        });


        path("/user", () -> {
            path("/orders", () -> {
                // GET  http://localhost:8080/user/orders/info-mail/:mail
                get(
                        "/info-mail/:mail",
                        (request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(orderService.sendInfoMail(request.params("mail")));
                        },
                        responseTransformer
                );

                // GET  http://localhost:8080/user/orders/category/most-popular
                get(
                        "/category/most-popular",
                        (request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(orderService.getMostPopularCategory());
                        },
                        responseTransformer
                );

                // GET  http://localhost:8080/user/orders/average-price/2023-01-01/2035-12-31
                get(
                        "/average-price/:from/:to",
                        (request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            LocalDate from = LocalDate.parse(request.params("from"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                            LocalDate to = LocalDate.parse(request.params("to"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                            return new ResponseDto<>(orderService.getAvgPriceOfOrdersOrderedBetween(from, to));
                        },
                        responseTransformer
                );

                // GET  http://localhost:8080/user/orders/category/most-expensive
                get(
                        "/category/most-expensive",
                        (request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(orderService.mostExpensiveProductFromEachCategory());
                        },
                        responseTransformer
                );
            });
        });

        path("/all", () -> {
            // GET http://localhost:8080/all/get-all
            get(
                    "/get-all",
                    (request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(orderService.getAll());
                    },
                    responseTransformer
            );
        });



    }
}
