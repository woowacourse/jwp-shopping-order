package cart.dao.dto;

import cart.dao.entity.ProductEntity;

import java.time.LocalDateTime;

public class OrderResultMap {

    private final Long orderId;
    private final Long orderItemId;
    private final ProductEntity productEntity;
    private final LocalDateTime date;
    private final int quantity;
    private final int price;

    public OrderResultMap(final Long orderId, final Long orderItemId, final ProductEntity productEntity, final LocalDateTime date, final int quantity, final int price) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.productEntity = productEntity;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
