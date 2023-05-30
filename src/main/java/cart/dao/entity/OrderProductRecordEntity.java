package cart.dao.entity;

import java.util.Objects;

public class OrderProductRecordEntity {

    private final Long id;
    private final Long orderProductId;
    private final Long productId;

    public OrderProductRecordEntity(Long orderProductId, Long productId) {
        this(null, orderProductId, productId);
    }

    public OrderProductRecordEntity(Long id, Long orderProductId, Long productId) {
        this.id = id;
        this.orderProductId = orderProductId;
        this.productId = productId;
    }

    public OrderProductRecordEntity assignId(Long id) {
        return new OrderProductRecordEntity(id, orderProductId, productId);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderProductId() {
        return orderProductId;
    }

    public Long getProductId() {
        return productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderProductRecordEntity that = (OrderProductRecordEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
