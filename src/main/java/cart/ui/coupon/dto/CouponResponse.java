package cart.ui.coupon.dto;

import cart.application.service.coupon.dto.CouponResultDto;

import java.util.List;
import java.util.stream.Collectors;

public class CouponResponse {

    private final Long id;
    private final String couponName;
    private final Integer discountPercent;
    private final Integer discountAmount;
    private final Integer minAmount;

    public CouponResponse(final Long id, final String couponName, final Integer discountPercent, final Integer discountAmount, final Integer minAmount) {
        this.id = id;
        this.couponName = couponName;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
    }

    public static List<CouponResponse> from(final List<CouponResultDto> couponResultDtos) {
        return couponResultDtos.stream()
                .map(couponResultDto -> new CouponResponse(
                        couponResultDto.getId(),
                        couponResultDto.getCouponName(),
                        couponResultDto.getDiscountPercent(),
                        couponResultDto.getDiscountAmount(),
                        couponResultDto.getMinAmount())
                )
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

}
