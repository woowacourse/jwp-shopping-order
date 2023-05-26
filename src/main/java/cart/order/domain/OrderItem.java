package cart.order.domain;

import static cart.order.exception.OrderExceptionType.INVALID_ORDER_ITEM_PRODUCT_QUANTITY;

import cart.order.exception.OrderException;

public class OrderItem {

    private final Long id;
    private final int quantity;
    private final Long productId;
    private final String name;
    private final int productPrice;
    private final String imageUrl;

    public OrderItem(int quantity, Long productId, String name, int productPrice, String imageUrl) {
        this(null, quantity, productId, name, productPrice, imageUrl);
    }

    public OrderItem(Long id, int quantity, Long productId, String name, int productPrice, String imageUrl) {
        validateQuantity(quantity);
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.name = name;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new OrderException(INVALID_ORDER_ITEM_PRODUCT_QUANTITY);
        }
    }

    public int price() {
        return productPrice * quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
