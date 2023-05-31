package cart.dto;

import cart.domain.Orders;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersDto {

    private final Long id;
    private final BigInteger totalItemsPrice;
    private final BigInteger discountPrice;
    private final Long deliveryFee;
    private final List<OrderItemDto> orderItems;

    public OrdersDto(final Orders orders) {
        this.id = orders.getId();
        this.totalItemsPrice = orders.getCalculateDiscountPrice().toBigInteger();
        this.discountPrice = orders.getDiscountPrice().toBigInteger();
        this.deliveryFee = orders.getDeliveryFee();
        this.orderItems = orders.getOrderItems().stream()
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
