package cart.domain.order;

import cart.domain.Coupon;
import cart.exception.CouponDiscountOverPriceException;

import java.util.List;

public class OrderPrice {
    private static final int MIN_ORDER_PRICE = 1;
    private final int productPrice;
    private int increasePrice = 0;
    private int discountPrice = 0;

    public OrderPrice(final List<OrderProduct> products) {
        productPrice = products.stream().
                mapToInt(OrderProduct::price)
                .sum();
    }

    public void apply(final Coupon coupon) {
        final int expectPrice = calculatePrice() - coupon.calculateDiscount();
        if (expectPrice < MIN_ORDER_PRICE) {
            throw new CouponDiscountOverPriceException("쿠폰 할인 금액이 상품 계산 값보다 더 큽니다.");
        }
        discount(coupon.calculateDiscount());
    }

    public void calculateDeliveryFee(final DeliveryFee deliveryFee) {
        increase(deliveryFee.getDeliveryFee());
    }

    private void discount(final int discountAmount) {
        final int discountedPrice = calculatePrice() - discountAmount;
        if (discountedPrice < MIN_ORDER_PRICE) {
            throw new IllegalArgumentException("상품 금액 이상으로 할인이 적용되었습니다.");
        }
        discountPrice += discountAmount;
    }

    private void increase(final int increaseAmount) {
        increasePrice += increaseAmount;
    }

    public int calculatePrice() {
        return productPrice + increasePrice - discountPrice;
    }
}
