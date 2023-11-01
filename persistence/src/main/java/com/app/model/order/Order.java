package com.app.model.order;

import com.app.model.customer.Customer;
import com.app.model.product.Product;
import com.app.model.product.ProductConverter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@ToString
@EqualsAndHashCode
public class Order {
    final Long id;
    final Customer customer;
    final Product product;
    final int quantity;
    final LocalDate orderDate;

    private Order(Long id, Customer customer, Product product, int quantity, LocalDate orderDate) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    public boolean isQuantityEqual(int quantity){
        return this.quantity == quantity;
    }

    public boolean isOrderDateBetween(LocalDate from, LocalDate to){
        return (orderDate.equals(from) || orderDate.isAfter(from)) && (orderDate.equals(to) || orderDate.isBefore(to));
    }

    public BigDecimal getTotalPriceWithDiscount(int minAge, int maxAge, BigDecimal ageDisc, int daysDifference, BigDecimal dateDisc){
        if(customer.hasAgeBetween(minAge,maxAge)){
            return this.getTotalPrice().multiply(ageDisc);
        }

        if(orderDate.isAfter(LocalDate.now().plusDays(daysDifference))){
            return this.getTotalPrice().multiply(dateDisc);
        }

        return this.getTotalPrice();
    }

    public static Order of(long id, Customer customer, Product product, int quantity, LocalDate orderDate){
        return new Order(id, customer,product,quantity,orderDate);
    }

    public BigDecimal getTotalPrice(){
        return ProductConverter.toPrice.apply(this.product).multiply(BigDecimal.valueOf(this.quantity));

    }


}
