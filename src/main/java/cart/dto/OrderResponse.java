package cart.dto;

import cart.domain.OrderedItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private List<OrderedItemResponse> orderedItems;
    private LocalDateTime orderedAt;
    private int totalItemPrice;
    private int discountedTotalItemPrice;
    private int shippingFee;
    private int totalPrice;

    public OrderResponse(Long id, List<OrderedItemResponse> orderedItems, LocalDateTime orderedAt, int totalItemPrice, int discountedTotalItemPrice, int shippingFee, int totalPrice) {
        this.id = id;
        this.orderedItems = orderedItems;
        this.orderedAt = orderedAt;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    public OrderResponse() {
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
}
