package com.woowahan.techcourse.order.coupon;

import com.woowahan.techcourse.coupon.application.CouponQueryService;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceRequestDto;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceRequestDto.OrderRequest;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceResponseDto;
import com.woowahan.techcourse.order.domain.ActualPriceCalculator;
import com.woowahan.techcourse.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class CouponPriceCalculator implements ActualPriceCalculator {

    private final CouponQueryService couponQueryService;

    public CouponPriceCalculator(CouponQueryService couponQueryService) {
        this.couponQueryService = couponQueryService;
    }

    @Override
    public long calculate(Order order) {
        OrderRequest orderInfo = new OrderRequest(order.calculateOriginalPrice());
        CalculateActualPriceRequestDto requestDto = new CalculateActualPriceRequestDto(orderInfo, order.getCouponIds());
        CalculateActualPriceResponseDto responseDto = couponQueryService.calculateActualPrice(requestDto);
        return responseDto.getActualPrice();
    }
}
