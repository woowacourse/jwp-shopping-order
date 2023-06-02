package cart.entity;

public class OrderItemEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productNameAtOrder;
    private final int productPriceAtOrder;
    private final String productImageUrlAtOrder;
    private final int quantity;

    public OrderItemEntity(
            final Long id,
            final Long orderId,
            final Long productId,
            final String productName,
            final int productPrice,
            final String productImageUrl,
            final int quantity
    ) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.productNameAtOrder = productName;
        this.productPriceAtOrder = productPrice;
        this.productImageUrlAtOrder = productImageUrl;
        this.quantity = quantity;
    }

    public static OrderItemEntity toCreate(
            final Long orderId,
            final Long productId,
            final String productName,
            final int productPrice,
            final String imageUrl,
            final int quantity
    ) {
        return new OrderItemEntity(
                null,
                orderId,
                productId,
                productName,
                productPrice,
                imageUrl,
                quantity
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductNameAtOrder() {
        return productNameAtOrder;
    }

    public int getProductPriceAtOrder() {
        return productPriceAtOrder;
    }

    public String getProductImageUrlAtOrder() {
        return productImageUrlAtOrder;
    }

    public int getQuantity() {
        return quantity;
    }
}
