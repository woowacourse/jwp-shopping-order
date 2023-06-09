package cart.dao.entity;

import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final int price;
    @Nullable
    private final Long couponId;
    private final Long memberId;
    private final Timestamp date;

    public OrderEntity(int price, Long couponId, Long memberId) {
        this(null, price, couponId, memberId, Timestamp.valueOf(LocalDateTime.now()));
    }

    public OrderEntity(Long id, int price, Long couponId, Long memberId, final Timestamp date) {
        this.id = id;
        this.price = price;
        this.couponId = couponId;
        this.memberId = memberId;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Timestamp getDate() {
        return date;
    }
}
