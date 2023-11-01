package com.app.service;


import com.app.config.AppTestBeansConfig;
import com.app.data.repository.orders.transaction.impl.OrderDataFromDbImpl;
import com.app.model.dto.service.SendEmailDto;
import com.app.service.email.EmailService;
import com.app.service.order.impl.OrderServiceImpl;
import com.app.service.order.provider.impl.OrdersProviderImpl;
import com.app.service.order.validator.orderDto.OrderDtoValidator;
import com.app.service.pdf.PdfServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.app.service.OrderDataProvider.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource(locations = "classpath:application_test.properties")
public class SendInfoMailTest {
    @Mock
    OrderDataFromDbImpl orderDataFromDb;
    @Mock
    EmailService emailService;
    @Mock
    PdfServiceImpl pdfService;
    @Mock
    OrderDtoValidator orderDtoValidator;
    @Mock
    OrdersProviderImpl ordersProvider;
    @Mock
    Environment environment;


    @Test
    @DisplayName("When sendInfoMail method is called, and in order list doesn't exist customer with same email as inputted.")
    void test1(){
        when(ordersProvider.provide())
                .thenReturn(List.of(orderA,orderB));

        OrderServiceImpl orderService = new OrderServiceImpl(ordersProvider,pdfService,emailService,orderDataFromDb,orderDtoValidator,environment);

        Assertions.assertThatThrownBy(() -> orderService.sendInfoMail("zzzz@gmail.com"))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("There's no customer with email same as inputted.");
    }
    
    @Test
    @DisplayName("When sendInfoMail method is called, and in order list existing customer with same email as inputted.")
    void test2(){
        when(ordersProvider.provide())
                .thenReturn(List.of(orderA,orderB));

        when(environment.getRequiredProperty("info_mail.email.body")).thenReturn("A");
        when(environment.getRequiredProperty("info_mail.pdf.name")).thenReturn("A");
        when(environment.getRequiredProperty("info_mail.pdf.content")).thenReturn("A");
        OrderServiceImpl orderService = new OrderServiceImpl(ordersProvider,pdfService,emailService,orderDataFromDb,orderDtoValidator,environment);

        Assertions.assertThat(orderService.sendInfoMail("aaaa@gmail.com"))
                .isEqualTo(new SendEmailDto("Info mail to aaaa@gmail.com sent"));
    }


}
