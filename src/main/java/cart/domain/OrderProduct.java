package cart.domain;

public class OrderProduct {

    private Long id;
    private OrderHistory orderHistory;
    private Long productId;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;

    public OrderProduct(final OrderHistory orderHistory, final Long productId, final String name, final int price, final String imageUrl,
                        final int quantity) {
        this(null, orderHistory, productId, name, price, imageUrl, quantity);
    }

    public OrderProduct(final Long id, OrderHistory orderHistory, final Long productId, final String name, final int price, final String imageUrl,
                        final int quantity) {
        this.id = id;
        this.orderHistory = orderHistory;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public OrderHistory getOrderHistory() {
        return orderHistory;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
