package cart.dto;

import cart.domain.Order;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersDto {

    private final Long id;
    private final BigInteger totalItemsPrice;
    private final BigInteger discountPrice;
    private final Long deliveryFee;
    private final List<OrderItemDto> orderItems;

    public OrdersDto(final Order order) {
        this.id = order.getId();
        this.totalItemsPrice = order.getCalculateDiscountPrice().toBigInteger();
        this.discountPrice = order.getDiscountPrice().toBigInteger();
        this.deliveryFee = order.getDeliveryFee();
        this.orderItems = order.getOrderItems().stream()
                .map(it -> new OrderItemDto(it.getId(), it.getName(), it.getPrice(), it.getImageUrl(),
                        it.getQuantity()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public BigInteger getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public BigInteger getDiscountPrice() {
        return discountPrice;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }
}
