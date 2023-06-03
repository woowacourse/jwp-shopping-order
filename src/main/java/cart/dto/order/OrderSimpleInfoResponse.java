package cart.dto.order;

import cart.domain.order.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderSimpleInfoResponse {
    private final Long id;
    private final int finalPrice;
    private final List<String> products;
    private final LocalDateTime createdAt;

    private OrderSimpleInfoResponse(final Long id,
                                   final int finalPrice,
                                   final List<String> products,
                                   final LocalDateTime createdAt) {
        this.id = id;
        this.finalPrice = finalPrice;
        this.products = products;
        this.createdAt = createdAt;
    }

    public static OrderSimpleInfoResponse from(final Order order) {
        final List<String> productNames = order.getOrderItems().stream()
                .map(orderItem -> orderItem.getProduct().getName())
                .collect(Collectors.toUnmodifiableList());

        return new OrderSimpleInfoResponse(
                order.getId(),
                order.getDiscountedPrice().getValue(),
                productNames,
                order.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public List<String> getProducts() {
        return products;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
