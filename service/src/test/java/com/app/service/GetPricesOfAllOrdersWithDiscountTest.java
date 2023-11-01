package com.app.service;



import com.app.config.AppTestBeansConfig;
import com.app.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class GetPricesOfAllOrdersWithDiscountTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("When getPricesOfAllOrdersWithDiscount() method iss called and generate specified output.")
    void test1(){
        Assertions.assertThat(orderService.getPricesOfAllOrdersWithDiscount())
                .isEqualTo(new BigDecimal("1933100.00"));
    }


}
