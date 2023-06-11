package cart.entity;

public class OrderItemEntity {
    private final Long id;
    private final Long productId;
    private final Long shoppingOrderId;
    private final String productNameAtOrder;
    private final int productPriceAtOrder;
    private final String productImageUrlAtOrder;
    private final int quantity;

    public OrderItemEntity(Long id, Long productId, Long shoppingOrderId, String productNameAtOrder,
                           int productPriceAtOrder, String productImageUrlAtOrder, int quantity) {
        this.id = id;
        this.productId = productId;
        this.shoppingOrderId = shoppingOrderId;
        this.productNameAtOrder = productNameAtOrder;
        this.productPriceAtOrder = productPriceAtOrder;
        this.productImageUrlAtOrder = productImageUrlAtOrder;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getShoppingOrderId() {
        return shoppingOrderId;
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
