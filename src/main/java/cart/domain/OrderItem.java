package cart.domain;

public class OrderItem {
    private final Long productId;
    private final String name;
    private final int price;
    private final String image;
    private final int quantity;

    public OrderItem(final Long productId, final String name, final int price, final String image, final int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public static OrderItem of(final CartItem cartItem) {
        return new OrderItem(
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getQuantity()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }
}
