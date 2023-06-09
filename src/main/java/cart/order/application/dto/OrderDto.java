package cart.order.application.dto;

import cart.order.domain.OrderHistory;

import java.util.List;

public class OrderDto {

    private OrderHistory orderHistory;
    private List<OrderedProductDto> products;

    public OrderDto(final OrderHistory orderHistory, final List<OrderedProductDto> products) {
        this.orderHistory = orderHistory;
        this.products = products;
    }

    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public List<OrderedProductDto> getProducts() {
        return products;
    }
}
