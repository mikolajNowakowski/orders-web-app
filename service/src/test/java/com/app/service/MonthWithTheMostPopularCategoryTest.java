package com.app.service;

import com.app.config.AppTestBeansConfig;
import com.app.service.order.OrderService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static com.app.model.product.category.Category.*;
import static java.time.Month.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class MonthWithTheMostPopularCategoryTest {


    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("When method monthWithTheMostPopularCategory() is called and should generate specified output")
    void test1(){

        Assertions.assertThat(orderService.monthsWithTheMostPopularCategory())
                .isEqualTo(Map.of(NOVEMBER, List.of(C),
                        FEBRUARY, List.of(B),
                        DECEMBER, List.of(A),
                        AUGUST, List.of(C),
                        JULY, List.of(B),
                        MAY, List.of(A)));
    }

}
