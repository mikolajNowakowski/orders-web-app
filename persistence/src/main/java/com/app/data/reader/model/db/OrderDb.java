package com.app.data.reader.model.db;

import com.app.data.reader.model.order.OrderData;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class OrderDb implements Identifiable {
    private Long id;
    private CustomerDb customerDb;
    private ProductDb productDb;
    private int quantity;
    private String orderDate;

    public OrderData toOrderData(){
        return OrderData.of(id,customerDb.toCustomerData(),productDb.toProductData(),String.valueOf(quantity),orderDate);
    }

    public static OrderDb of(Long id, CustomerDb customerDb, ProductDb productDb, int quantity, String orderDate){
        return new OrderDb(id, customerDb, productDb, quantity, orderDate);
    }

}
