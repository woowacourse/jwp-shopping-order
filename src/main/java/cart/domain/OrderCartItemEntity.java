package cart.domain;

public class OrderCartItemEntity {
    private final Long id;
    private final Long orderedId;
    private final String name;
    private final int quantity;
    private final String imageUrl;
    private final int totalPrice;
    private final int totalDiscountPrice;

    public OrderCartItemEntity(Long id, Long orderedId, String name, int quantity,
                               String imageUrl, int totalPrice, int totalDiscountPrice) {
        this.id = id;
        this.orderedId = orderedId;
        this.name = name;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.totalPrice = totalPrice;
        this.totalDiscountPrice = totalDiscountPrice;
    }

    public static OrderCartItemEntity of(Long orderedId, OrderCartItem orderCartItem) {
        return new OrderCartItemEntity(
                null,
                orderedId,
                orderCartItem.getCartItem().getProduct().getName(),
                orderCartItem.getCartItem().getQuantity(),
                orderCartItem.getCartItem().getProduct().getImageUrl(),
                orderCartItem.getOriginalPrice().getValue(),
                orderCartItem.getDiscountPrice().getValue()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderedId() {
        return orderedId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTotalDiscountPrice() {
        return totalDiscountPrice;
    }
}
