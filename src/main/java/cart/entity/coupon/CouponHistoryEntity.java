package cart.entity.coupon;

public class CouponHistoryEntity {

    private final Long id;
    private final String name;
    private final Long orderTableId;

    public CouponHistoryEntity(final Long id, final String name, final Long orderTableId) {
        this.id = id;
        this.name = name;
        this.orderTableId = orderTableId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
