package cart.entity;

import cart.domain.MemberCoupon;
import cart.domain.OrderItem;
import cart.domain.Product;
import java.util.List;

public class OrderItemEntity {

    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String productName;
    private Integer productPrice;
    private String productImageUrl;
    private Integer totalPrice;

    public OrderItemEntity(Long orderId, Long productId, Integer quantity, String productName, Integer productPrice,
                           String productImageUrl, Integer totalPrice) {
        this(null, orderId, productId, quantity, productName, productPrice, productImageUrl, totalPrice);
    }

    public OrderItemEntity(Long id, Long orderId, Long productId, Integer quantity, String productName,
                           Integer productPrice, String productImageUrl, Integer totalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.totalPrice = totalPrice;
    }

    public static OrderItemEntity from(OrderItem orderItem, Long orderId) {
        Product product = orderItem.getProduct();
        return new OrderItemEntity(orderId, product.getId(), orderItem.getQuantity(), product.getName(),
                product.getPrice(), product.getImageUrl(), orderItem.getTotalPrice());
    }

    public OrderItem toDomain(List<MemberCoupon> memberCoupons) {
        return new OrderItem(id, new Product(productId, productName, productPrice, productImageUrl), quantity,
                memberCoupons, totalPrice);
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

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
