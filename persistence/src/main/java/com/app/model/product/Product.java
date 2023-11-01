package com.app.model.product;

import com.app.model.product.category.Category;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
public class Product {
    final Long id;
    final String name;
    final BigDecimal price;
    final Category category;

    private Product(Long id, String name, BigDecimal price, Category category) {
        this.id=id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
    public static Product of(Long id, String name, BigDecimal price, Category category){
        return new Product(id, name, price, category);
    }
}
