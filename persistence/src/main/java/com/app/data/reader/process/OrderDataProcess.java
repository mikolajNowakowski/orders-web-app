package com.app.data.reader.process;

import com.app.model.order.Order;

import java.util.*;

public sealed interface OrderDataProcess extends DataProcess<List<Order>> permits OrderDataDbProcessImpl, OrderDataJsonProcessImpl, OrderDataTxtProcessImpl {
}
