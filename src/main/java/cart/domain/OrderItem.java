package cart.domain;

public class OrderItem {
    private final Long id;
    private final String name;
    private final int quantity;
    private final String imageUrl;
    private final Money orderPrice;

    public OrderItem(Long id, String name, int quantity, String imageUrl, Money orderPrice) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
    }

    public OrderItem(String name, int quantity, String imageUrl, Money orderPrice) {
        this(null, name, quantity, imageUrl, orderPrice);
    }

    public static OrderItem from(CartItem cartItem) {
        int quantity = cartItem.getQuantity();
        Product product = cartItem.getProduct();
        int price = product.getPrice();
        Money orderPrice = Money.from(price * quantity);
        return new OrderItem(product.getName(), quantity, product.getImageUrl(), orderPrice);
    }

    public Money getOrderPrice() {
        return orderPrice;
    }
}
