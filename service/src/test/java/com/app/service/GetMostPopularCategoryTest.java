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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.app.model.product.category.Category.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class GetMostPopularCategoryTest {


    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("When method getMostPopularCategory() is called and should generate specified output")
    void test1() {


        Assertions
                .assertThat(orderService
                        .getMostPopularCategory().stream().collect(Collectors.toSet()))
                .isEqualTo(Set.of(C, A, B));

    }
}
