package com.app.data.process;

import com.app.config.AppTestBeansConfig;
import com.app.data.reader.model.filename.DataSource;
import com.app.data.reader.model.filename.OrderDBSource;
import com.app.data.reader.model.filename.OrderFilePath;
import com.app.data.reader.process.OrderDataProcess;
import com.app.data.reader.process.type.ProcessType;
import com.app.model.order.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static com.app.data.OrderDataProvider.*;
import static com.app.data.reader.process.type.ProcessType.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class ProcessTest {
    @Value("${orders_file_path.json}")
    private String ordersJsonPath;
    @Value("${orders_file_path.txt}")
    private String ordersTxtPath;
    @Autowired
    private OrderDataProcess orderDataDbProcessImpl;
    @Autowired
    private OrderDataProcess orderDataJsonProcessImpl;
    @Autowired
    private OrderDataProcess orderDataTxtProcessImpl;
    @Autowired
    private OrderDBSource orderDBSource;

    List<Order> output = List.of(orderA, orderB, orderC);

    @Test
    @DisplayName("When process method from orderDataJsonProcessImpl class is called and it produces expected output.")
    void test1(){
        Assertions.assertThat(orderDataJsonProcessImpl.process(new OrderFilePath(ordersJsonPath)))
                .isEqualTo(output);
    }

    @Test
    @DisplayName("When process method from orderDataDbProcessImpl class is called and it produces expected output.")
    void test2(){
        Assertions.assertThat(orderDataDbProcessImpl.process(orderDBSource))
                .isEqualTo(output);
    }

    @Test
    @DisplayName("When process method from orderDataTxtProcessImpl class is called and it produces expected output.")
    void test3(){
        Assertions.assertThat(orderDataTxtProcessImpl.process(new OrderFilePath(ordersTxtPath)))
                .isEqualTo(output);
    }

    @Test
    @DisplayName("When process method is called with null argument")
    void test4(){
        Assertions.assertThatThrownBy(() -> orderDataDbProcessImpl.process(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted argument is null.");
    }

    @Test
    @DisplayName("When process method is called with null argument")
    void test5(){
        Assertions.assertThatThrownBy(() -> orderDataJsonProcessImpl.process(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted argument is null.");
    }

    @Test
    @DisplayName("When process method is called with null argument")
    void test6(){
        Assertions.assertThatThrownBy(() -> orderDataTxtProcessImpl.process(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted argument is null.");
    }
}
