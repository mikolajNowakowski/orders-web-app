package com.app;

import com.app.config.AppConfig;

import com.app.router.ErrorRouter;
import com.app.router.OrderRouter;
import com.app.router.SecurityRouter;
import com.app.router.UserRouter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static spark.Spark.initExceptionHandler;
import static spark.Spark.port;

public class Main {

    public static void main(String[] args) {

        initExceptionHandler(err -> System.out.println(err.getMessage()));
        port(8080);
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var orderRouter = context.getBean("orderRouter", OrderRouter.class);
        var errorRouter = context.getBean("errorRouter", ErrorRouter.class);
        var usersRouter = context.getBean("userRouter", UserRouter.class);
        var securityRouter = context.getBean("securityRouter", SecurityRouter.class);
        orderRouter.routes();
        usersRouter.routes();
        securityRouter.routes();
        errorRouter.routes();


    }
}
