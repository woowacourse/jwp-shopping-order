package com.woowahan.techcourse.order.coupon;

import com.woowahan.techcourse.coupon.application.CouponQueryService;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceRequest;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceRequest.OrderRequest;
import com.woowahan.techcourse.coupon.application.dto.CalculateActualPriceResponse;
import com.woowahan.techcourse.order.domain.ActualPriceCalculator;
import com.woowahan.techcourse.order.domain.Order;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class CouponPriceCalculator implements ActualPriceCalculator {

    private final CouponQueryService couponQueryService;

    public CouponPriceCalculator(CouponQueryService couponQueryService) {
        this.couponQueryService = couponQueryService;
    }

    @Override
    public BigDecimal calculate(Order order) {
        OrderRequest orderInfo = new OrderRequest(order.calculateOriginalPrice());
        CalculateActualPriceRequest requestDto = new CalculateActualPriceRequest(orderInfo, order.getCouponIds());
        CalculateActualPriceResponse responseDto = couponQueryService.calculateActualPrice(requestDto);
        return responseDto.getActualPrice();
    }
}
