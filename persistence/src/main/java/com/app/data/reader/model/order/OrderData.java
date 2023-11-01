package com.app.data.reader.model.order;

import com.app.data.reader.model.customer.CustomerData;
import com.app.data.reader.model.product.ProductData;
import com.app.model.dto.order.OrderDto;
import com.app.model.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderData {

    Long id;
    CustomerData customer;
    ProductData product;
    String quantity;
    String orderDate;

    public Order toOrder() {
        return Order.of(this.id,
                this.customer.toCustomer(),
                this.product.toProduct(),
                Integer.parseInt(this.quantity),
                LocalDate.parse(this.orderDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        );
    }

public OrderDto toOrderDto(){
        return new OrderDto(customer.toCustomerDto(), product.toProductDto(),orderDate,quantity);
}

    public static OrderData of(Long id,CustomerData customer, ProductData product, String quantity, String orderDate) {
        return new OrderData(id,customer, product, quantity, orderDate);
    }
}
