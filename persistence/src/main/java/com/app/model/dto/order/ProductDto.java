package com.app.model.dto.order;


import com.app.data.reader.model.db.ProductDb;

import java.math.BigDecimal;

public record ProductDto(String name, String price, String category) {


    public ProductDb toProductDb(){
        return new ProductDb(null,name,new BigDecimal(price),category);
    }
}
