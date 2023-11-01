package com.app.service;


import com.app.config.AppTestBeansConfig;
import com.app.model.product.Product;
import com.app.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.app.model.product.category.Category.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class MostExpensiveProductFromEachCategoryTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("When method mostExpensiveProductFromEachCategory() is called.")
    void test1(){
        Assertions.assertThat(orderService.mostExpensiveProductFromEachCategory())
                .isEqualTo(Map.of(A, List.of(Product.of(4L,"BIKED",new BigDecimal("21000.00"),A)),
                        C, List.of(Product.of(6L,"BIKEF",new BigDecimal("96000.00"),C)),
                        B, List.of(Product.of(5L,"BIKEE",new BigDecimal("22000.00"),B))));
    }
}
