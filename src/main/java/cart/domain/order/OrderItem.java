package cart.domain.order;

import cart.domain.Product;

public class OrderItem {

    private final Long id;
    private final Long orderId;
    private final String productName;
    private final String productImage;
    private final int productPrice;
    private final int productQuantity;

    public OrderItem(Long id, final Long orderId, final String productName, final String productImage, final int productPrice, final int productQuantity) {
        this.id = id;
        this.orderId = orderId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public static OrderItem of(final Long orderId, final int quantity, final Product product) {
        return new OrderItem(
                null,
                orderId,
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                quantity
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }
}
