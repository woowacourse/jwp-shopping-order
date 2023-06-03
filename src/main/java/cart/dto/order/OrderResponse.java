package cart.dto.order;

import cart.domain.bill.Bill;
import cart.domain.member.Member;
import cart.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private List<OrderItemDto> orderedItems;
    private LocalDateTime orderedAt;
    private int totalItemDiscountAmount;
    private int totalMemberDiscountAmount;
    private int totalItemPrice;
    private int discountedTotalItemPrice;
    private int shippingFee;
    private int totalPrice;

    public OrderResponse(
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

    public static OrderResponse from(final Order order, final Member member, final Bill bill) {
        List<OrderItemDto> orderItemDtos = orderItemsToOrderItemDtos(order);
        return new OrderResponse(
                order.getId(),
                orderItemDtos,
                order.getGenerateTime(),
                order.getOrderItems().getItemBenefit().getMoney(),
                order.getOrderItems().getMemberBenefit(member).getMoney(),
                bill.getTotalItemPrice(),
                bill.getDiscountedTotalItemPrice(),
                bill.getShippingFee(),
                bill.getDiscountedTotalItemPrice() + bill.getShippingFee()
        );
    }

    private static List<OrderItemDto> orderItemsToOrderItemDtos(final Order order) {
        return order.getOrderItems().getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
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
