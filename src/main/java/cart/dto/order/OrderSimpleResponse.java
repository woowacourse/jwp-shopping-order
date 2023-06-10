package cart.dto.order;

import cart.domain.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderSimpleResponse {
    private final Long id;
    private final Integer finalPrice;
    private final List<String> products;
    private final LocalDateTime createdAt;


    private OrderSimpleResponse(final Long id, final Integer finalPrice, final List<String> products,
                               final LocalDateTime createdAt) {
        this.id = id;
        this.finalPrice = finalPrice;
        this.products = products;
        this.createdAt = createdAt;
    }

    public static OrderSimpleResponse from(Order order) {
        return new OrderSimpleResponse(
                order.getId(),
                order.getPayment().getFinalPrice().getValue(),
                getProductNames(order),
                order.getCreatedAt());
    }

    private static List<String> getProductNames(final Order order) {
        List<String> productNames = order.getOrderItems().getItems().stream()
                .map(orderItem -> orderItem.getProduct().getName())
                .collect(Collectors.toList());
        return productNames;
    }

    public Long getId() {
        return id;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public List<String> getProducts() {
        return products;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
