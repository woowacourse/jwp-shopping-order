package cart.dto;

import cart.domain.Product;
import cart.domain.order.OrderItem;

public class OrderDto {

    private Long orderId;
    private Long productId;
    private String productName;
    private int productPrice;
    private String productImageUrl;
    private Long orderItemId;
    private int productQuantity;

    public OrderDto(final Long orderId, final Long productId, final String productName,
        final int productPrice, final String productImageUrl, final Long orderItemId, final int productQuantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.orderItemId = orderItemId;
        this.productQuantity = productQuantity;
    }

    public Long getOrderId() {
        return orderId;
    }
    
    public Product getProduct() {
        return new Product(productId, productName, productPrice, productImageUrl);
    }

    public OrderItem getOrderItem() {
        return OrderItem.persisted(orderItemId, getProduct(), productQuantity);
    }
}
