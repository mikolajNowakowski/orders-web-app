package com.app.persistence_config;

import com.app.data.reader.factory.FromDbToOrderWithValidation;
import com.app.data.reader.factory.FromJsonToOrderWithValidation;
import com.app.data.reader.factory.FromTxtToOrderWithValidation;
import com.app.data.repository.orders.transaction.impl.OrderDataFromDbImpl;
import com.app.data.reader.model.filename.OrderDBSource;
import com.app.data.reader.process.OrderDataDbProcessImpl;
import com.app.data.reader.process.OrderDataJsonProcessImpl;
import com.app.data.reader.process.OrderDataProcess;
import com.app.data.reader.process.OrderDataTxtProcessImpl;
import com.app.transformer.LocalDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
@Configuration
@ComponentScan("com.app")
@PropertySource({"classpath:application.properties"})
@RequiredArgsConstructor
public class AppConfig {

    private final Environment environment;

    @Bean
    public Jdbi jdbi() {
        var URL = environment.getRequiredProperty("db.url");
        var USERNAME = environment.getRequiredProperty("db.username");
        var PASSWORD = environment.getRequiredProperty("db.password");
        return Jdbi.create(URL, USERNAME, PASSWORD);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }
  @Bean
  public OrderDataProcess orderDataDbProcessImpl(FromDbToOrderWithValidation fromDbToOrderWithValidation){
        return new OrderDataDbProcessImpl(fromDbToOrderWithValidation);
  }

    @Bean
    public OrderDataProcess orderDataJsonProcessImpl(FromJsonToOrderWithValidation fromJsonToOrderWithValidation){
        return new OrderDataJsonProcessImpl(fromJsonToOrderWithValidation);
    }

    @Bean
    public OrderDataProcess orderDataTxtProcessImpl(FromTxtToOrderWithValidation fromTxtToOrderWithValidation){
        return new OrderDataTxtProcessImpl(fromTxtToOrderWithValidation);
    }

    @Bean
    public OrderDBSource orderDBSource(Jdbi jdbi){
        return new OrderDBSource(jdbi);
    }

    @Bean
    public OrderDataFromDbImpl orderDataFromDb(Jdbi jdbi){
        return new OrderDataFromDbImpl(jdbi);
    }
}
