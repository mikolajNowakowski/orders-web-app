package com.app.model.product;
import com.app.model.product.category.Category;
import java.math.BigDecimal;
import java.util.function.Function;

public interface ProductConverter {
    Function<Product, BigDecimal> toPrice = product -> product.price;
    Function<Product, Category> toCategory = product -> product.category;
}
