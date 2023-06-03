package cart.dto;

import cart.domain.MemberCoupon;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CouponResponse {

    private List<RateCouponResponse> rateCoupon;
    private List<FixedCouponResponse> fixedCoupon;

    public CouponResponse(List<RateCouponResponse> rateCoupon, List<FixedCouponResponse> fixedCoupon) {
        this.rateCoupon = rateCoupon;
        this.fixedCoupon = fixedCoupon;
    }

    public static CouponResponse from(List<MemberCoupon> memberCoupons) {
        List<RateCouponResponse> rateCouponResponses = memberCoupons.stream()
                .filter(memberCoupon -> memberCoupon.getCoupon().getCouponType() == CouponType.RATE)
                .map(RateCouponResponse::from)
                .collect(Collectors.toList());
        List<FixedCouponResponse> fixedCouponResponses = memberCoupons.stream()
                .filter(memberCoupon -> memberCoupon.getCoupon().getCouponType() == CouponType.FIXED)
                .map(FixedCouponResponse::from)
                .collect(Collectors.toList());
        return new CouponResponse(rateCouponResponses, fixedCouponResponses);
    }

    public List<RateCouponResponse> getRateCoupon() {
        return rateCoupon;
    }

    public List<FixedCouponResponse> getFixedCoupon() {
        return fixedCoupon;
    }

    static class RateCouponResponse {

        private final Long id;
        private final String name;
        private final BigDecimal discountRate;
        private final LocalDate expiredDate;
        private final BigDecimal minOrderPrice;

        public RateCouponResponse(Long id, String name, BigDecimal discountRate, LocalDate expiredDate,
                                  BigDecimal minOrderPrice) {
            this.id = id;
            this.name = name;
            this.discountRate = discountRate;
            this.expiredDate = expiredDate;
            this.minOrderPrice = minOrderPrice;
        }

        public static RateCouponResponse from(MemberCoupon memberCoupon) {
            Coupon coupon = memberCoupon.getCoupon();
            return new RateCouponResponse(
                    memberCoupon.getId(),
                    coupon.getName(),
                    coupon.getDiscountValue(),
                    memberCoupon.getExpiredDate(),
                    coupon.getMinOrderPrice().getValue()
            );
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getDiscountRate() {
            return discountRate;
        }

        public LocalDate getExpiredDate() {
            return expiredDate;
        }

        public BigDecimal getMinOrderPrice() {
            return minOrderPrice;
        }
    }

    static class FixedCouponResponse {

        private final Long id;
        private final String name;
        private final BigDecimal discountPrice;
        private final LocalDate expiredDate;
        private final BigDecimal minOrderPrice;

        public FixedCouponResponse(Long id, String name, BigDecimal discountPrice, LocalDate expiredDate,
                                   BigDecimal minOrderPrice) {
            this.id = id;
            this.name = name;
            this.discountPrice = discountPrice;
            this.expiredDate = expiredDate;
            this.minOrderPrice = minOrderPrice;
        }

        public static FixedCouponResponse from(MemberCoupon memberCoupon) {
            Coupon coupon = memberCoupon.getCoupon();
            return new FixedCouponResponse(
                    memberCoupon.getId(),
                    coupon.getName(),
                    coupon.getDiscountValue(),
                    memberCoupon.getExpiredDate(),
                    coupon.getMinOrderPrice().getValue()
            );
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getDiscountPrice() {
            return discountPrice;
        }

        public LocalDate getExpiredDate() {
            return expiredDate;
        }

        public BigDecimal getMinOrderPrice() {
            return minOrderPrice;
        }
    }


}
