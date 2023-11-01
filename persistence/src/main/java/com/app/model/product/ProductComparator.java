package com.app.model.product;

import java.util.Comparator;

public interface ProductComparator {
    Comparator<Product> byPrice = Comparator.comparing(product -> product.price);
}
