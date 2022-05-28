package tacos.data;

import tacos.pojo.Order;

public interface OrderRepository {

    Order save(Order order);
}
