package com.app.data.reader.model.db;

import com.app.data.reader.model.customer.CustomerData;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class CustomerDb implements Identifiable {
    private Long id;
    private String name;
    private String surname;
    private int age;
    private String email;

    public CustomerData toCustomerData() {
        return new CustomerData(id,name, surname, String.valueOf(age), email);
    }

    public static CustomerDb of(Long id, String name, String surname, int age, String email) {
        return new CustomerDb(id, name, surname, age, email);
    }
}
