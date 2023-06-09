package cart.domain;

public class OrderItem {

    private final Long id;
    private final String name;
    private final int productPrice;
    private final int quantity;
    private final String imageUrl;

    public OrderItem(Long id, String name, int productPrice, int quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public OrderItem(String name, int productPrice, int quantity, String imageUrl) {
        this(null, name, productPrice, quantity, imageUrl);
    }

    public static OrderItem from(CartItem cartItem) {
        final Product product = cartItem.getProduct();

        return new OrderItem(product.getName(), product.getPrice(), cartItem.getQuantity(), product.getImageUrl());
    }

    public int getPrice() {
        return productPrice * quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
