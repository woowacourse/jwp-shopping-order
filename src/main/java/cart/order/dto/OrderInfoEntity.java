package cart.order.dto;

import cart.order.domain.OrderInfo;
import cart.product.domain.Product;

public class OrderInfoEntity {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final Long quantity;
    
    public OrderInfoEntity(final Long id, final Long orderId, final Long productId, final String name, final Long price, final String imageUrl, final Long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }
    
    public static OrderInfoEntity of(final Long orderId, final OrderInfo orderInfo) {
        final Product product = orderInfo.getProduct();
        return new OrderInfoEntity(
                orderInfo.getId(),
                orderId,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                orderInfo.getQuantity()
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
    
    public String getName() {
        return name;
    }
    
    public Long getPrice() {
        return price;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public Long getQuantity() {
        return quantity;
    }
    
    @Override
    public String toString() {
        return "OrderInfoEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
