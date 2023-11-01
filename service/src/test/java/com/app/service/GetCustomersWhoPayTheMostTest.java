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
import static com.app.service.OrderDataProvider.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class GetCustomersWhoPayTheMostTest {

    @Autowired
    private OrderService orderService;


    @Test
    @DisplayName("When method mostExpensiveProductFromEachCategory() is called.")
    void test1(){
        Assertions.assertThat(orderService.getCustomersWhoPayTheMost())
                .isEqualTo(List.of(customerF));
    }
}
