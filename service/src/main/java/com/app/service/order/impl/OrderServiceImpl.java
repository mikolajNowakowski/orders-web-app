package com.app.service.order.impl;

import com.app.data.repository.orders.transaction.impl.OrderDataFromDbImpl;
import com.app.data.writer.JsonWriter;
import com.app.model.comparing_type.ComparingType;
import com.app.model.customer.Customer;
import com.app.model.dto.order.OrderDto;
import com.app.model.dto.service.SendEmailDto;
import com.app.model.order.Order;
import com.app.model.product.Product;
import com.app.model.product.category.Category;
import com.app.service.email.EmailService;
import com.app.service.order.OrderService;
import com.app.service.order.provider.OrdersProvider;
import com.app.service.order.validator.orderDto.OrderDtoValidator;
import com.app.service.pdf.PdfService;
import com.itextpdf.text.Font;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.app.model.customer.CustomerConverter.*;
import static com.app.model.order.OrderConverter.*;
import static com.app.model.product.ProductConverter.*;
import static com.itextpdf.text.Element.*;
import static com.itextpdf.text.Font.FontFamily.*;
import static java.util.stream.Collectors.*;

@Service
public final class OrderServiceImpl implements OrderService {

    private final ArrayList<Order> orders;
    private final PdfService pdfService;
    private final EmailService emailService;
    private final OrderDataFromDbImpl orderDataFromDb;
    private final OrderDtoValidator orderDtoValidator;
    private final Environment environment;


    public OrderServiceImpl(OrdersProvider ordersProvider, PdfService pdfService, EmailService emailService, OrderDataFromDbImpl orderDataFromDb, OrderDtoValidator orderDtoValidator, Environment environment) {
        this.orders = new ArrayList<>(ordersProvider.provide());
        this.pdfService = pdfService;
        this.emailService = emailService;
        this.orderDataFromDb = orderDataFromDb;
        this.orderDtoValidator = orderDtoValidator;
        this.environment = environment;
    }

    /**
     * @return Collection of all orders.
     */
    @Override
    public List<Order> getAll() {
        return orders;
    }


    /**
     * @param from Star date.
     * @param to   End date.
     * @return Average price of products ordered within a specific time frame.
     */
    @Override
    public BigDecimal getAvgPriceOfOrdersOrderedBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("One of inputted argument is null");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("The start of te date range cannot be after the end");
        }

        var prices = orders
                .stream()
                .filter(order -> order.isOrderDateBetween(from, to))
                .map(Order::getTotalPrice)
                .toList();

        return prices.size() == 0 ? BigDecimal.ZERO :
                prices
                        .stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal
                                .valueOf(prices.size()), RoundingMode.HALF_UP);
    }


    /**
     * @return Map where key is category and value is list of most expensive products from in that category.
     */
    @Override
    public Map<Category, List<Product>> mostExpensiveProductFromEachCategory() {
        return orders
                .stream()
                .map(toProduct)
                .collect(groupingBy(toCategory))
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey,
                        entry -> getMostExpensiveProducts(entry.getValue())));
    }


    /**
     * @return List of customers who pay the most for all orders.
     */
    @Override
    public List<Customer> getCustomersWhoPayTheMost() {
        return orders
                .stream()
                .collect(collectingAndThen(groupingBy(Order::getTotalPrice), map -> map
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByKey())
                        .orElseThrow()))
                .getValue()
                .stream()
                .map(toCustomer)
                .toList();
    }


    /**
     * @param email Customer's email to which the list of their products is to be sent.
     * @return Information on which email address the information has been sent to.
     */
    @Override
    public SendEmailDto sendInfoMail(String email) {
        var customers = orders.stream().map(toCustomer).filter(customer -> customer.hasEmailAs(email)).toList();

        if (customers.isEmpty()) {
            throw new IllegalArgumentException("There's no customer with email same as inputted.");
        }

        var emailBody = environment.getRequiredProperty("info_mail.email.body");
        var pdfName = environment.getRequiredProperty("info_mail.pdf.name");
        var pdfContent = environment.getRequiredProperty("info_mail.pdf.content");

        var productsInfo = orders
                .stream()
                .filter(order -> toCustomer.apply(order).equals(customers.get(0)))
                .map(toProduct)
                .distinct()
                .map(Product::toString).
                collect(joining("\n"));

        emailService.sendWithAttachment(
                toNameSurname.apply(customers.get(0)),
                email,
                "%s Product Info".formatted(toNameSurname.apply(customers.get(0))),
                emailBody,
                pdfName,
                pdfService.generateMail(pdfService.generateParagraph(pdfContent,
                        new Font(HELVETICA, 18, Font.BOLD),
                        ALIGN_CENTER), pdfService.generateParagraph(productsInfo,
                        new Font(TIMES_ROMAN, 12, Font.NORMAL),
                        ALIGN_CENTER))
        );
        return new SendEmailDto("Info mail to %s sent".formatted(email));
    }


    /**
     * @param comparingType An argument that decides finding the minimum or maximum number of orders.
     * @return List of Dates when minimum or maximum number of orders has been done.
     */
    @Override
    public List<LocalDate> getDateWithMinMaxPurchases(ComparingType comparingType) {
        if (comparingType == null) {
            throw new IllegalArgumentException("Inputted argument is null.");
        }

        var datesWithOrders = orders
                .stream()
                .collect(groupingBy(toOrderDate));

        var ordersNumber = switch (comparingType) {
            case MAX -> datesWithOrders
                    .entrySet()
                    .stream()
                    .max(Comparator.comparing(a -> a.getValue().size()))
                    .map(entry -> entry.getValue().size())
                    .orElseThrow();
            case MIN -> datesWithOrders
                    .entrySet()
                    .stream()
                    .min(Comparator.comparing(a -> a.getValue().size()))
                    .map(entry -> entry.getValue().size())
                    .orElseThrow();
        };

        return datesWithOrders
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() == ordersNumber)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * @return Total price of all orders including discounts.
     */
    @Override
    public BigDecimal getPricesOfAllOrdersWithDiscount() {
        return orders
                .stream()
                .map(order -> order.getTotalPriceWithDiscount(0, 25, BigDecimal.valueOf(0.97), 2, BigDecimal.valueOf(0.98)))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }


    /**
     * @param n        Number of products the customer purchased.
     * @param filepath File path to pdf file with customers data whu purchased specified number of products.
     * @return Number of customers who purchased specified number of products.
     */
    @Override
    public int numberOfCustomersWhoBoughtTheSameProductSpecifiedTimesAndWriteToJson(int n, String filepath) {

        if (n < 0) {
            throw new IllegalArgumentException("Number of purchases of the product must be positive.");
        }

        if (filepath == null) {
            throw new IllegalArgumentException("Inputted json file path is null.");
        }

        var customersWithSpecifiedOrders = orders
                .stream()
                .filter(order -> order.isQuantityEqual(n))
                .map(toCustomer)
                .distinct()
                .toList();

        JsonWriter.saveToJsonFile(customersWithSpecifiedOrders, filepath);

        return customersWithSpecifiedOrders.size();
    }

    /**
     * @return List of the most popular categories.
     */
    @Override
    public List<Category> getMostPopularCategory() {
        return getTheMostPopularCategoryBasicOnOrders(this.orders);
    }


    /**
     * @return Map sorted in descending order by value, where key is Month and value is number of products ordered in that month.
     */
    @Override
    public LinkedHashMap<Month, Integer> monthsWithNumberOfOrderedProducts() {
        return orders
                .stream()
                .collect(collectingAndThen(
                        groupingBy(toMonth),
                        map -> map.entrySet()
                                .stream()
                                .collect(toMap(
                                        Map.Entry::getKey,
                                        entry -> entry.getValue().stream().map(toProduct).distinct().toList().size(),
                                        (v1, v2) -> v2,
                                        LinkedHashMap::new)
                                )
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Month, Integer>comparingByValue().reversed())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2, LinkedHashMap::new));
    }


    /**
     * @return Map where key is Mont and value is list of te most popular category in that month.
     */
    @Override
    public Map<Month, List<Category>> monthsWithTheMostPopularCategory() {
        return orders
                .stream()
                .collect(collectingAndThen(groupingBy(toMonth),
                        map -> map
                                .entrySet()
                                .stream()
                                .collect(toMap(Map.Entry::getKey,
                                        entry -> getTheMostPopularCategoryBasicOnOrders(entry.getValue())))));
    }


    /**
     * @param orderDto All data of new order.
     * @return Added order data.
     */
    @Override
    public OrderDto save(OrderDto orderDto) {

        var errorMap = orderDtoValidator.validate(orderDto);

        if (!errorMap.isEmpty()) {
            throw new IllegalArgumentException("Errors during orderDto validation:\n%s ".formatted(errorMap
                    .entrySet()
                    .stream()
                    .map((entry) -> "%s -> %s".formatted(entry.getKey(), entry.getValue()))
                    .collect(joining("\n"))));
        }

        var addedOrderData = orderDataFromDb.save(orderDto);

        orders.add(addedOrderData.toOrder());

        return addedOrderData.toOrderDto();
    }


    private List<Category> getTheMostPopularCategoryBasicOnOrders(List<Order> orders) {
        var categoryWithOccurrenceCount = orders
                .stream()
                .map(toProduct)
                .collect(groupingBy(toCategory, counting()));

        var maxOccurrence = categoryWithOccurrenceCount
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElseThrow();

        return categoryWithOccurrenceCount
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(maxOccurrence))
                .map(Map.Entry::getKey)
                .toList();
    }


    private Customer findByMail(List<Customer> customers, String mail) {
        return customers.stream().dropWhile(customer -> !toMail.apply(customer).equals(mail)).toList().get(0);
    }

    private List<Product> getMostExpensiveProducts(List<Product> products) {
        return products
                .stream()
                .collect(groupingBy(toPrice))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElseThrow();
    }

    private Properties readPropertiesFile(String path) {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
