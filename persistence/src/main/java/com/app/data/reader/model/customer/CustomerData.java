package com.app.data.reader.model.customer;

import com.app.model.customer.Customer;
import com.app.model.dto.order.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerData {

    Long id;
    String name;
    String surname;
    String age;
    String email;
    public Customer toCustomer() {
        return Customer.of(this.id,this.name, this.surname, Integer.parseInt(this.age), this.email);
    }

    public CustomerDto toCustomerDto(){
        return new CustomerDto(name,surname,age,email);
    }


    public static CustomerData of(long id,String name, String surname, String age, String email){
        return new CustomerData(id,name,surname,age,email);
    }
}
