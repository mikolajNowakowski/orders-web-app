package com.app.data.validator;

import com.app.config.AppTestBeansConfig;
import com.app.data.OrderDataProvider;
import com.app.data.reader.model.customer.CustomerData;
import com.app.data.reader.model.order.OrderData;
import com.app.data.reader.model.product.ProductData;
import com.app.data.reader.model.product.category.CategoryData;
import com.app.data.reader.validator.OrderDataValidator;
import com.app.data.reader.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.app.data.OrderDataProvider.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class ValidateTest {

    @Autowired
    private Validator<List<OrderData>> orderDataValidator;



    @Test
    @DisplayName("When convert() method is called with correct argument.")
    void test1(){

        Assertions
                .assertThatThrownBy(
                        () -> orderDataValidator.
                                validate(null)

                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted data is null.");
    }




    @Test()
    @DisplayName("When validate() is called with correct arguments and every object has correct fields.")
    void test2(){
        List<OrderData> data = List.of(orderDataA,orderDataB,orderDataC);
        Assertions
                .assertThat(orderDataValidator.validate(data))
                .isEqualTo(data);
    }
    @Test()
    @DisplayName("When validate() is called with correct arguments and every object has correct fields.")
    void test3(){

    var wrongData1 = OrderData.of(1L,CustomerData.of(1L,"aAA", "AAAA", "50", "aaaa@gmail.com"),
            ProductData.of(1L,"BIKEA", "15000.00", CategoryData.A), "2", "17-07-2035");

        var wrongData2 = OrderData.of(1L,CustomerData.of(1L,"AAA", "aaaa", "50", "aaaa@gmail.com"),
                ProductData.of(1L,"BIKEA", "15000.00", CategoryData.A), "2", "17-07-2035");

        var wrongData3 = OrderData.of(1L,CustomerData.of(1L,"AAA", "AAAA", "-50", "HBGIB32432432!!!!!aaaa@gmail.com"),
                ProductData.of(1L,"BIKEA", "15000.00", CategoryData.A), "2", "17-07-2035");

        var wrongData4 = OrderData.of(1L,CustomerData.of(1L,"AAA", "AAAA", "-50", "aaaa@gmail.com"),
                ProductData.of(1L,"BIKEa", "15000.00", CategoryData.A), "2", "17-07-2035");

        var wrongData5 = OrderData.of(1L,CustomerData.of(1L,"AAA", "AAAA", "-50", "aaaa@gmail.com"),
                ProductData.of(1L,"BIKEA", "-15000.00", CategoryData.A), "-10", "17-07-2035");

        var wrongData6 = OrderData.of(1L,CustomerData.of(1L,"AAA", "AAAA", "-50", "aaaa@gmail.com"),
                ProductData.of(1L,"BIKEA", "15000.00", CategoryData.A), "10", "17-07-1035");


        List<OrderData> data = List.of(orderDataA,orderDataB,orderDataC,wrongData1,wrongData2,wrongData3,wrongData4,wrongData5,wrongData6);
        Assertions
                .assertThat(orderDataValidator.validate(data))
                .isEqualTo(List.of(orderDataA,orderDataB,orderDataC));
    }



}
