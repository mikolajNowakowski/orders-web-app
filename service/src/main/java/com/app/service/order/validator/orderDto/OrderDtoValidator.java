package com.app.service.order.validator.orderDto;

import com.app.model.dto.order.OrderDto;
import com.app.service.order.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public  class OrderDtoValidator implements Validator<OrderDto> {

    @Value("${validator.productNameRegex}")
    private  String productNameRegex;
    @Value("${validator.nameSurnameRegex}")
    private  String nameSurnameRegex;
    @Value("${validator.emailRegex}")
    private  String emailRegex;

    @Override
    public Map<String, String> validate(OrderDto orderDto) {
        var errorMap = new HashMap<String, String>();

        if(!orderDto.CustomerDto().name().matches(nameSurnameRegex)){
            errorMap.put("Name","Incorrect form of customer name.");
        }

        if(!orderDto.CustomerDto().surname().matches(nameSurnameRegex)){
            errorMap.put("Surname","Incorrect form of customer surname.");
        }

        if(!orderDto.CustomerDto().email().matches(emailRegex)){
            errorMap.put("Email","Incorrect form of customer email.");
        }

        if(!orderDto.CustomerDto().age().matches("^[1-9]\\d*$")){
            errorMap.put("Customer age","Wrong representation of customer age. Age must be represented by positive numerical value.");
        }

        if(!orderDto.ProductDto().name().matches(productNameRegex)){
            errorMap.put("Product name","Incorrect form of product name.");
        }

        if(!orderDto.ProductDto().price().matches("^[1-9]\\d+(\\.\\d{2})?$")){
            errorMap.put("Product price","Incorrect form of product price.");
        }

        if(!orderDto.quantity().matches("^[1-9]\\d*$")){
            errorMap.put("Order quantity","Quantity value must be positive.");
        }

        if(!(LocalDate.parse(orderDto.date(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).toEpochDay() >=
                LocalDate.now().toEpochDay())){
            errorMap.put("Order date","The order date must be a future date relative to the current one.");
        }

        return errorMap;
    }
}
