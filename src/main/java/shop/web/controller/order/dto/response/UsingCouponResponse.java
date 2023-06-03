package shop.web.controller.order.dto.response;

import shop.application.coupon.dto.CouponDto;

public class UsingCouponResponse {
    private String name;
    private Integer discountRate;

    private UsingCouponResponse() {
    }

    private UsingCouponResponse(String name, Integer discountRate) {
        this.name = name;
        this.discountRate = discountRate;
    }

    public static UsingCouponResponse of(CouponDto couponDto) {
        return new UsingCouponResponse(couponDto.getName(), couponDto.getDiscountRate());
    }

    public String getName() {
        return name;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }
}
