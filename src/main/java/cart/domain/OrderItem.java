package cart.domain;

public class OrderItem {

    private final Long id;
    private final String name;
    private final int price;
    private final int quantity;
    private final String imageUrl;

    public OrderItem(Long id, String name, int price, int quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public OrderItem(String name, int price, int quantity, String imageUrl) {
        this(null, name, price, quantity, imageUrl);
    }

    public static OrderItem from(CartItem cartItem) {
        final Product product = cartItem.getProduct();

        return new OrderItem(product.getName(), product.getPrice(), cartItem.getQuantity(), product.getImageUrl());
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

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
