package com.app.data.repository.orders.transaction.impl;

import com.app.data.repository.generic.CrudRepository;
import com.app.data.repository.orders.impl.CustomerDbRepository;
import com.app.data.repository.orders.impl.ProductDbRepository;
import com.app.data.repository.orders.transaction.OrderDataFromDb;
import com.app.data.reader.model.db.CustomerDb;
import com.app.data.reader.model.db.Identifiable;
import com.app.data.reader.model.db.OrderDb;
import com.app.data.reader.model.db.ProductDb;
import com.app.data.reader.model.order.OrderData;
import com.app.model.dto.order.CustomerDto;
import com.app.model.dto.order.OrderDto;
import com.app.model.dto.order.ProductDto;
import org.jdbi.v3.core.Jdbi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class OrderDataFromDbImpl implements OrderDataFromDb<OrderData> {
    private final Jdbi jdbi;
    private final ProductDbRepository productDbRepository;
    private final CustomerDbRepository customerDbRepository;

    public OrderDataFromDbImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.productDbRepository = new ProductDbRepository(jdbi);
        this.customerDbRepository = new CustomerDbRepository(jdbi);
    }

    @Override
    public List<OrderData> getAll() {
        var productsMap = getElementsWithIds(productDbRepository.findAll());
        var customersMap = getElementsWithIds(customerDbRepository.findAll());
        var sql = "SELECT * FROM orders";

        return CrudRepository.findAllWithMapping(jdbi, sql, ((rs, ctx) -> OrderDb.builder()
                .id(rs.getLong("id"))
                .orderDate(rs.getString("order_date"))
                .quantity(rs.getInt("quantity"))
                .productDb(productsMap.get(rs.getLong("product_id")))
                .customerDb(customersMap.get(rs.getLong("customer_id")))
                .build())).stream().map(OrderDb::toOrderData).toList();
    }

    @Override
    public OrderData save(OrderDto orderDto) {
        Long productIdForAddedOrder = idOfAddedOrExistingProduct(orderDto.ProductDto());
        Long customerIdForAddedOrder = idOfAddedOrExistingCustomer(orderDto.CustomerDto());

        var saveOrderSql = "INSERT INTO orders (customer_id, product_id, quantity, order_date) VALUES (%s, %s, %s, '%s')"
                .formatted(customerIdForAddedOrder,
                        productIdForAddedOrder,
                        orderDto.quantity(),
                        orderDto.date());

        var insertedRows = jdbi.withHandle(handle -> handle.execute(saveOrderSql));

        if (insertedRows == 0) {
            throw new IllegalStateException("Row not inserted");
        }

        var getAddedDataSql = "select * from orders order by id desc limit :n";

        return CrudRepository.findOneWithMapping(jdbi, getAddedDataSql, 1, ((rs, ctx) -> {
            return OrderDb.builder()
                    .id(rs.getLong("id"))
                    .orderDate(rs.getString("order_date"))
                    .quantity(rs.getInt("quantity"))
                    .productDb(productDbRepository.findById(rs.getLong("product_id")).orElseThrow())
                    .customerDb(customerDbRepository.findById(rs.getLong("customer_id")).orElseThrow())
                    .build();
        })).toOrderData();
    }


    public OrderData findById(Long id) {
        String selectQuery = "SELECT * FROM orders WHERE id = :orderId";
        return jdbi.withHandle(handle ->
                        handle.createQuery(selectQuery)
                                .bind("orderId", id)
                                .map((rs, ctx) ->
                                        OrderDb.builder()
                                                .id(rs.getLong("id"))
                                                .orderDate(rs.getString("order_date"))
                                                .quantity(rs.getInt("quantity"))
                                                .productDb(productDbRepository.findById(rs.getLong("product_id")).orElseThrow())
                                                .customerDb(customerDbRepository.findById(rs.getLong("customer_id")).orElseThrow())
                                                .build())
                ).findFirst()
                .orElseThrow(() -> new IllegalStateException("No item to delete"))
                .toOrderData();
    }

    @Override
    public OrderData delete(Long id) {

        return jdbi.withHandle(handle -> {
            var itemToDelete = findById(id);

            var sql = "DELETE FROM orders WHERE id = :id";
            handle.createUpdate(sql)
                    .bind("id", id)
                    .execute();

            // Połączenie jest nadal otwarte po wykonaniu operacji DELETE
            return itemToDelete;
        });


//        var itemToDelete = findById(id);
//
//        var sql = "delete from orders where id = :id";
//        jdbi.useHandle(handle -> handle
//                .createUpdate(sql)
//                .bind("id", id)
//                .execute());
//        return itemToDelete;
    }


    private Long idOfAddedOrExistingCustomer(CustomerDto customerDto) {
        var customers = getElementsWithIds(customerDbRepository.findAll());
        var existingCustomer = customers
                .values()
                .stream()
                .filter(customerDb -> compareCustomers(customerDb, customerDto))
                .toList();

        if (existingCustomer.isEmpty()) {
            return customerDbRepository.save(customerDto.toCustomerDb()).getId();
        } else {
            return existingCustomer.stream().findFirst().orElseThrow().getId();
        }
    }

    private Long idOfAddedOrExistingProduct(ProductDto productDto) {
        var products = getElementsWithIds(productDbRepository.findAll());
        var existingCustomer = products
                .values()
                .stream()
                .filter(productDb -> compareProducts(productDb, productDto))
                .toList();

        if (existingCustomer.isEmpty()) {
            return productDbRepository.save(productDto.toProductDb()).getId();
        } else {
            return existingCustomer.stream().findFirst().orElseThrow().getId();
        }
    }

    private boolean compareProducts(ProductDb productDb, ProductDto productDto) {
        return productDb.getName().equals(productDto.name()) ||
                productDb.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).equals(new BigDecimal(productDto.price(), new MathContext(2)));
    }

    private boolean compareCustomers(CustomerDb customerDb, CustomerDto customerDto) {
        return customerDb.getName().equals(customerDto.name()) ||
                customerDb.getEmail().equals(customerDto.email());
    }

    public static <T extends Identifiable> Map<Long, T> getElementsWithIds(List<T> data) {
        return data
                .stream()
                .collect(Collectors.toMap(Identifiable::getId,
                        Function.identity()));
    }
}
