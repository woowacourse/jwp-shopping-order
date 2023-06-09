package cart.dto;

import cart.domain.Order;
import cart.domain.OrderedItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private List<OrderedItemResponse> orderedItems;
    private LocalDateTime orderedAt;

    private int totalItemDiscountAmount;
    private int totalMemberDiscountAmount;
    private int totalItemPrice;
    private int discountedTotalItemPrice;
    private int shippingFee;
    private int totalPrice;

    private OrderResponse(Long id, List<OrderedItemResponse> orderedItems, LocalDateTime orderedAt, int totalItemDiscountAmount,
                         int totalMemberDiscountAmount, int totalItemPrice, int discountedTotalItemPrice, int shippingFee, int totalPrice) {
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

    public OrderResponse() {
    }

    public static OrderResponse of(Order order, List<OrderedItem> orderedItems, int totalItemDiscountedAmount, int totalMemberDiscountAmount){
        return new OrderResponse(order.getId(), convertToResponse(orderedItems), order.getOrderedAt(), totalItemDiscountedAmount, totalMemberDiscountAmount, order.getTotalItemPrice(),
                order.getDiscountedTotalItemPrice(), order.getShippingFee(),
                order.getDiscountedTotalItemPrice() + order.getShippingFee());
    }

    public static List<OrderedItemResponse> convertToResponse(List<OrderedItem> orderedItems) {
        List<OrderedItemResponse> list = new ArrayList<>();
        for (OrderedItem orderedItem : orderedItems) {
            OrderedItemResponse orderedItemResponse = createOrderedItemResponse(orderedItem);
            list.add(orderedItemResponse);
        }
        return list;
    }

    public static OrderedItemResponse createOrderedItemResponse(OrderedItem orderedItem) {
        return OrderedItemResponse.of(orderedItem);
    }

    public Long getId() {
        return id;
    }

    public List<OrderedItemResponse> getOrderedItems() {
        return orderedItems;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
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

    public int getTotalItemDiscountAmount() {
        return totalItemDiscountAmount;
    }

    public int getTotalMemberDiscountAmount() {
        return totalMemberDiscountAmount;
    }
}
