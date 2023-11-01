package com.app.model.dto.order;


public record OrderDto(com.app.model.dto.order.CustomerDto CustomerDto,
                       ProductDto ProductDto,
                       String date,
                       String quantity) {


}
