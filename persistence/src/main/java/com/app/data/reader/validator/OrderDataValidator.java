package com.app.data.reader.validator;

import com.app.data.reader.model.customer.CustomerData;
import com.app.data.reader.model.order.OrderData;
import com.app.data.reader.model.product.ProductData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.app.data.reader.validator.Validator.*;
import static java.lang.Integer.*;

@Component
@RequiredArgsConstructor
public final class OrderDataValidator implements Validator<List<OrderData>> {
    @Value("${validator.productNameRegex}")
    private  String productNameRegex;
    @Value("${validator.nameSurnameRegex}")
    private  String nameSurnameRegex = "[A-Z ]+";
    @Value("${validator.emailRegex}")
    private  String emailRegex;

    @Override
    public List<OrderData> validate(List<OrderData> data) {

        if(data==null){
            throw new IllegalArgumentException("Inputted data is null.");
        }
        return data
                .stream()
                .filter(this::validateOrder)
                .toList();
    }

    private boolean validateOrder(OrderData order) {
        return validateCustomer(order.getCustomer()) &&
                validateProduct(order.getProduct()) &&
                isGreaterThanOrEqual(parseInt(order.getQuantity()), 1) &&
                LocalDate.parse(order.getOrderDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).toEpochDay() >=
                        LocalDate.now().toEpochDay()
                && order.getCustomer().getEmail().matches(emailRegex);

    }

    private boolean validateProduct(ProductData product) {
        return isBigDecimalPositive(product.getPrice()) &&
                validateExpressionWithRegex(product.getName(), productNameRegex);
    }

    private boolean validateCustomer(CustomerData customer) {
        return validateExpressionWithRegex(customer.getName(), nameSurnameRegex) &&
                validateExpressionWithRegex(customer.getSurname(), nameSurnameRegex) &&
                isGreaterThanOrEqual(parseInt(customer.getAge()), 18);
    }
}
