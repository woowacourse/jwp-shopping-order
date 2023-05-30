package cart.entity;

public class OrderProductEntity {

    private final Long id;
    private final long orderHistoryId;
    private final Long productId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;

    public OrderProductEntity(
            final Long id,
            final long orderHistoryId,
            final Long productId,
            final String name,
            final int price,
            final String imageUrl,
            final int quantity
    ) {
        this.id = id;
        this.orderHistoryId = orderHistoryId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public OrderProductEntity(
            final long orderHistoryId,
            final Long productId,
            final String name,
            final int price,
            final String imageUrl,
            final int quantity
    ) {
        this(null, orderHistoryId, productId, name, price, imageUrl, quantity);
    }

    public Long getId() {
        return id;
    }

    public long getOrderHistoryId() {
        return orderHistoryId;
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
