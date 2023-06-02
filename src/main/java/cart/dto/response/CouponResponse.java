package cart.dto.response;

import cart.domain.Coupon;

import java.util.List;
import java.util.stream.Collectors;

public class CouponResponse {

    private final long id;
    private final String name;
    private final int discountPrice;

    public CouponResponse(final long id, final String name, final int discountPrice) {
        this.id = id;
        this.name = name;
        this.discountPrice = discountPrice;
    }

    public static List<CouponResponse> of(final List<Coupon> coupons) {
        return coupons.stream()
                .map(coupon -> new CouponResponse(coupon.getId(), coupon.getCouponInfo(), coupon.getDiscountPrice()))
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
