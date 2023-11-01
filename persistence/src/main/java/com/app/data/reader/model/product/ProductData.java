package com.app.data.reader.model.product;


import com.app.data.reader.model.product.category.CategoryData;
import com.app.model.dto.order.ProductDto;
import com.app.model.product.Product;
import com.app.model.product.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductData {
    Long id;
    String name;
    String price;
    CategoryData category;

    public Product toProduct(){
        return Product.of(this.id,this.name, new BigDecimal(this.price), Category.valueOf(category.toString()));
    }

    public ProductDto toProductDto(){
        return new ProductDto(name,price,category.toString());
    }

    public static ProductData  of(Long id,String name, String price, CategoryData category){
        return new ProductData(id,name,price,category);
    }
}
