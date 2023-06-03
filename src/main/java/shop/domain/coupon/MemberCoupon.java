package shop.domain.coupon;

import shop.domain.member.Member;
import shop.exception.ShoppingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MemberCoupon {
    private final Long id;
    private final Member owner;
    private final Coupon coupon;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiredAt;
    private final boolean isUsed;

    public MemberCoupon(Long id, Member owner, Coupon coupon, LocalDateTime issuedAt,
                        LocalDateTime expiredAt, boolean isUsed) {
        this.id = id;
        this.owner = owner;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
    }

    public static MemberCoupon issue(Member owner, Coupon coupon) {
        LocalDateTime issuedAt = LocalDateTime.now();
        int period = coupon.getPeriod();
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(period);

        return new MemberCoupon(null, owner, coupon, issuedAt, expiredAt, Boolean.FALSE);
    }

    public void checkAvailability() {
        if (isUsed) {
            throw new ShoppingException("이미 사용한 쿠폰입니다.");
        }
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(expiredAt)) {
            String expiredDate = expiredAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));

            throw new ShoppingException("사용 기간이 만료된 쿠폰입니다. " +
                    "사용 만료 기간 : " + expiredDate);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return owner.getId();
    }

    public Long getCouponId() {
        return coupon.getId();
    }

    public String getCouponName() {
        return coupon.getName();
    }

    public Integer getDiscountRate() {
        return coupon.getDiscountRate();
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getMemberCouponExpiredAt() {
        return expiredAt;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
