package cart.domain;

public class OrderItem {
    private final Long id;
    private final String name;
    private final int quantity;
    private final String imageUrl;
    private final Money totalPrice;

    public OrderItem(Long id, String name, int quantity, String imageUrl, Money totalPrice) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public OrderItem(String name, int quantity, String imageUrl, Money totalPrice) {
        this(null, name, quantity, imageUrl, totalPrice);
    }

    public static OrderItem from(CartItem cartItem) {
        int quantity = cartItem.getQuantity();
        Product product = cartItem.getProduct();
        int price = product.getPrice();
        Money orderPrice = Money.from(price * quantity);
        return new OrderItem(product.getName(), quantity, product.getImageUrl(), orderPrice);
    }

    public Long getId() {
        return id;
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

    public Money getTotalPrice() {
        return totalPrice;
    }
}
