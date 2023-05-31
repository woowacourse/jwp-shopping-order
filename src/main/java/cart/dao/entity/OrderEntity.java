package cart.dao.entity;

import java.time.LocalDateTime;

public class OrderEntity {
    private Long id;
    private Long memberId;
    private Long payment;
    private Long discountPoint;

    public OrderEntity(final Long id, final Long memberId, final Long payment, final Long discountPoint) {
        this.id = id;
        this.memberId = memberId;
        this.payment = payment;
        this.discountPoint = discountPoint;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPayment() {
        return payment;
    }

    public Long getDiscountPoint() {
        return discountPoint;
    }
}
