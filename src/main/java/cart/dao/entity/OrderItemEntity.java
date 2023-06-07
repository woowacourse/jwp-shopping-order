package cart.dao.entity;

import cart.domain.cartitem.CartItem;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItemEntity {

    private final Long id;
    private final Long orderId;
    private final Long memberId;
    private final Long productId;
    private final String name;
    private final BigDecimal price;
    private final String imageUrl;
    private final int quantity;

    public OrderItemEntity(
            Long orderId,
            Long memberId,
            Long productId,
            String name,
            BigDecimal price,
            String imageUrl,
            int quantity
    ) {
        this(null, orderId, memberId, productId, name, price, imageUrl, quantity);
    }

    public OrderItemEntity(
            Long id,
            Long orderId,
            Long memberId,
            Long productId,
            String name,
            BigDecimal price,
            String imageUrl,
            int quantity
    ) {
        this.id = id;
        this.orderId = orderId;
        this.memberId = memberId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItemEntity from(Long orderId, CartItem cartItem) {
        return new OrderItemEntity(
                orderId,
                cartItem.getMember().getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName().getValue(),
                cartItem.getProduct().getPrice().getValue(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getQuantity().getValue()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return quantity == that.quantity && Objects.equals(orderId, that.orderId) && Objects.equals(memberId, that.memberId) && Objects.equals(productId, that.productId) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, memberId, productId, name, price, imageUrl, quantity);
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", memberId=" + memberId +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
