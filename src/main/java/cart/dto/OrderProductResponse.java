package cart.dto;

public class OrderProductResponse {
    private final Long productId;
    private final String name;
    private final Integer quantity;
    private final Integer price;
    private final Integer totalPrice;

    public OrderProductResponse(Long productId, String name, Integer quantity, Integer price, Integer totalPrice) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
