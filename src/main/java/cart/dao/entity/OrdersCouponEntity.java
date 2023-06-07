package cart.dao.entity;

import java.util.Objects;

public class OrdersCouponEntity {
    private final Long id;
    private final Long ordersId;
    private final Long couponId;

    public OrdersCouponEntity(Long id, Long ordersId, Long couponId) {
        this.id = id;
        this.ordersId = ordersId;
        this.couponId = couponId;
    }

    public Long getId() {
        return id;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public Long getCouponId() {
        return couponId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersCouponEntity that = (OrdersCouponEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
