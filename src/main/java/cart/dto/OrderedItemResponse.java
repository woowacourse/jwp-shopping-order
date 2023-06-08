package cart.dto;

import cart.domain.CartItem;
import cart.domain.OrderedItem;

public class OrderedItemResponse {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;
    private int discountRate;
    private int discountedPrice;

    private OrderedItemResponse(Long id, String name, int price, String imageUrl, int quantity, int discountRate, int discountedPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
    }

    public OrderedItemResponse() {
    }

    public static OrderedItemResponse of(OrderedItem orderedItem) {
        return new OrderedItemResponse(
                orderedItem.getId(),
                orderedItem.getName(),
                orderedItem.getPrice(),
                orderedItem.getImageUrl(),
                orderedItem.getQuantity(),
                orderedItem.getDiscountRate(),
                orderedItem.calculateDiscountedPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }
}
