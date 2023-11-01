package com.app.data.reader.model.db;

import com.app.data.reader.model.product.ProductData;
import com.app.data.reader.model.product.category.CategoryData;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductDb implements Identifiable {
    private Long id;
    private String name;
    private BigDecimal price;
    private String category;

    public ProductData toProductData() {
        return ProductData.of(id,name, price.toPlainString(), CategoryData.valueOf(category));
    }

    public static ProductDb of(Long id, String name, BigDecimal price, String category){
        return new ProductDb(id,name,price,category);
    }

}
