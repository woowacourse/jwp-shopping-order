package cart.ui.dto;

import cart.domain.Order;
import java.time.LocalDateTime;
import java.util.List;

public class OrderShowResponse {

    private Long id;
    private List<OrderItemDto> orderedItems;
    private LocalDateTime orderedAt;
    private int totalItemDiscountAmount;
    private int totalMemberDiscountAmount;
    private int totalItemPrice;
    private int discountedTotalItemPrice;
    private int shippingFee;
    private int totalPrice;

    public OrderShowResponse(
            final Long id,
            final List<OrderItemDto> orderedItems,
            final LocalDateTime orderedAt,
            final int totalItemDiscountAmount,
            final int totalMemberDiscountAmount,
            final int totalItemPrice,
            final int discountedTotalItemPrice,
            final int shippingFee,
            final int totalPrice
    ) {
        this.id = id;
        this.orderedItems = orderedItems;
        this.orderedAt = orderedAt;
        this.totalItemDiscountAmount = totalItemDiscountAmount;
        this.totalMemberDiscountAmount = totalMemberDiscountAmount;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    public static OrderShowResponse of(
            final List<OrderItemDto> orderItemDtos,
            final Order order
    ) {
        return new OrderShowResponse(
                order.getId(),
                orderItemDtos,
                order.getOrderedAt(),
                order.calculateTotalItemDiscountAmount(),
                order.calculateTotalMemberDiscountAmount(),
                order.calculateTotalItemPrice(),
                order.calculateDiscountedTotalItemPrice(),
                order.getShippingFee(),
                order.calculateTotalPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemDto> getOrderedItems() {
        return orderedItems;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public int getTotalItemDiscountAmount() {
        return totalItemDiscountAmount;
    }

    public int getTotalMemberDiscountAmount() {
        return totalMemberDiscountAmount;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public int getDiscountedTotalItemPrice() {
        return discountedTotalItemPrice;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
