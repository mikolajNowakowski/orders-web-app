package com.app.config;

import com.app.data.reader.converter.Converter;
import com.app.data.reader.converter.OrderDataConverter;
import com.app.data.reader.factory.FromDbToOrderWithValidation;
import com.app.data.reader.factory.FromJsonToOrderWithValidation;
import com.app.data.reader.factory.FromTxtToOrderWithValidation;
import com.app.data.reader.loader.Loader;
import com.app.data.reader.loader.db.OrderDbDataLoader;
import com.app.data.reader.loader.json.OrdersJsonDataLoader;
import com.app.data.repository.orders.impl.CustomerDbRepository;
import com.app.data.repository.orders.impl.ProductDbRepository;
import com.app.data.repository.orders.transaction.impl.OrderDataFromDbImpl;
import com.app.data.reader.loader.txt.OrderTxtDataLoader;
import com.app.data.reader.model.filename.OrderDBSource;
import com.app.data.reader.model.order.OrderData;
import com.app.data.reader.process.OrderDataDbProcessImpl;
import com.app.data.reader.process.OrderDataJsonProcessImpl;
import com.app.data.reader.process.OrderDataProcess;
import com.app.data.reader.process.OrderDataTxtProcessImpl;
import com.app.data.reader.validator.OrderDataValidator;
import com.app.data.reader.validator.Validator;
import com.app.model.order.Order;
import com.app.service.email.EmailConfiguration;
import com.app.service.email.EmailService;
import com.app.service.email.EmailServiceImpl;
import com.app.service.order.OrderService;
import com.app.service.order.impl.OrderServiceImpl;
import com.app.service.order.provider.OrdersProvider;
import com.app.service.order.provider.impl.OrdersProviderImpl;
import com.app.service.order.validator.orderDto.OrderDtoValidator;
import com.app.service.pdf.PdfService;
import com.app.service.pdf.PdfServiceImpl;
import com.google.gson.GsonBuilder;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@TestPropertySource({"classpath:application_test.properties"})
public class AppTestBeansConfig {
    private final Environment environment;

    public AppTestBeansConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Jdbi jdbi() {
        var URL = environment.getRequiredProperty("db.url");
        var USERNAME = environment.getRequiredProperty("db.username");
        var PASSWORD = environment.getRequiredProperty("db.password");
        return Jdbi.create(URL, USERNAME, PASSWORD);
    }

    @Bean
    public Converter<List<OrderData>, List<Order>> orderDataConverter() {
        return new OrderDataConverter();
    }

    @Bean
    public OrderDbDataLoader orderDbDataLoader(OrderDataFromDbImpl orderDataFromDb) {
        return new OrderDbDataLoader(orderDataFromDb);
    }

    @Bean
    public Loader<List<OrderData>> ordersJsonDataLoader() {
        return new OrdersJsonDataLoader(new GsonBuilder().setPrettyPrinting().create());
    }

    @Bean
    public Loader<List<OrderData>> orderTxtDataLoader() {
        return new OrderTxtDataLoader();
    }


    @Bean
    public Validator<List<OrderData>> orderDataValidator() {
        return new OrderDataValidator();
    }

    @Bean
    public FromDbToOrderWithValidation fromDbToOrderWithValidation(Validator<List<OrderData>> orderDataValidator,
                                                                   OrderDbDataLoader orderDbDataLoader) {
        return new FromDbToOrderWithValidation(orderDataValidator, orderDbDataLoader);
    }

    @Bean
    public FromJsonToOrderWithValidation fromJsonToOrderWithValidation(Validator<List<OrderData>> orderDataValidator) {
        return new FromJsonToOrderWithValidation(orderDataValidator);
    }

    @Bean
    public FromTxtToOrderWithValidation fromTxtToOrderWithValidation(Validator<List<OrderData>> orderDataValidator) {
        return new FromTxtToOrderWithValidation(orderDataValidator);
    }


    @Bean
    public OrderDataProcess orderDataDbProcessImpl(FromDbToOrderWithValidation fromDbToOrderWithValidation) {
        return new OrderDataDbProcessImpl(fromDbToOrderWithValidation);
    }

    @Bean
    public OrderDataProcess orderDataJsonProcessImpl(FromJsonToOrderWithValidation fromJsonToOrderWithValidation) {
        return new OrderDataJsonProcessImpl(fromJsonToOrderWithValidation);
    }

    @Bean
    public OrderDataProcess orderDataTxtProcessImpl(FromTxtToOrderWithValidation fromTxtToOrderWithValidation) {
        return new OrderDataTxtProcessImpl(fromTxtToOrderWithValidation);
    }

    @Bean
    public OrderDBSource orderDBSource(Jdbi jdbi) {
        return new OrderDBSource(jdbi);
    }


    @Bean
    public OrdersProvider orderDataProvider(OrderDataProcess orderDataDbProcessImpl,
                                            OrderDataProcess orderDataJsonProcessImpl,
                                            OrderDataProcess orderDataTxtProcessImpl,
                                            OrderDBSource orderDBSource) {
        return new OrdersProviderImpl(orderDataTxtProcessImpl, orderDataJsonProcessImpl, orderDataDbProcessImpl, orderDBSource);
    }

    @Bean
    public PdfService pdfService() {
        return new PdfServiceImpl();
    }


    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl(null, new EmailConfiguration("x", "y"));
    }

    @Bean
    public OrderDataFromDbImpl orderDataFromDb(Jdbi jdbi) {
        return new OrderDataFromDbImpl(jdbi);
    }

    @Bean
    public OrderDtoValidator orderDtoValidator() {
        return new OrderDtoValidator();
    }

    @Bean("productDbRepositorySaveTest")
    public ProductDbRepository productDbRepository(Jdbi jdbi) {
        return new ProductDbRepository(jdbi);
    }

    @Bean("customerDbRepositorySaveTest")
    public CustomerDbRepository customerDbRepository(Jdbi jdbi) {
        return new CustomerDbRepository(jdbi);
    }

    @Bean("orderServiceSaveTest")
    public OrderService orderServiceSaveTest(EmailService emailService, PdfService pdfService, OrdersProvider ordersProvider, OrderDataFromDbImpl orderDataFromDbImpl, OrderDtoValidator orderDtoValidator, Environment environment) {
        return new OrderServiceImpl(ordersProvider, pdfService, emailService, orderDataFromDbImpl, orderDtoValidator, environment);
    }


    @Bean
    public OrderService orderService(EmailService emailService, PdfService pdfService, OrdersProvider ordersProvider, OrderDataFromDbImpl orderDataFromDbImpl, OrderDtoValidator orderDtoValidator, Environment environment) {
        return new OrderServiceImpl(ordersProvider, pdfService, emailService, orderDataFromDbImpl, orderDtoValidator, environment);
    }
}
