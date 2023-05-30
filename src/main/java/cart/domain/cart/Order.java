package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.dto.coupon.CouponResponse;
import cart.dto.history.ProductHistory;
import cart.dto.order.OrderResponse;
import cart.dto.product.DeliveryFeeResponse;
import cart.entity.order.ProductHistoryResponse;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Order {

    private final Timestamp timestamp;
    private final Member member;
    private final Cart cart;

    public Order(final Member member, final Cart cart) {
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.member = member;
        this.cart = cart;
    }

    public OrderResponse pay(final List<Long> productIds, final List<Integer> quantities, final List<Long> couponsId) {
        cart.validateBuying(productIds, quantities);
        member.validateHasCoupons(couponsId);

        List<ProductHistory> productHistories = cart.buy(productIds, quantities);
        List<Coupon> coupons = member.getCouponsByIds(couponsId);
        applyCoupon(productHistories, coupons);

        return generateOrderHistory(productHistories, coupons);
    }

    private OrderResponse generateOrderHistory(final List<ProductHistory> productHistories, final List<Coupon> coupons) {
        List<ProductHistoryResponse> productsResponse = productHistories.stream()
                .map(ProductHistoryResponse::from)
                .collect(Collectors.toList());

        List<CouponResponse> couponsResponse = coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        int deliveryFee = cart.calculateDeliveryFeeUsingCoupons(coupons);

        return new OrderResponse(timestamp.toString(), productsResponse, DeliveryFeeResponse.from(deliveryFee), couponsResponse);
    }

    private void applyCoupon(List<ProductHistory> productHistories, List<Coupon> coupons) {
        List<Coupon> productCoupons = coupons.stream()
                .filter(coupon -> !coupon.isDeliveryCoupon())
                .collect(Collectors.toList());

        productHistories.forEach(productHistory -> applyCouponsOnProduct(productCoupons, productHistory));
    }

    private void applyCouponsOnProduct(final List<Coupon> productCoupons, final ProductHistory productHistory) {
        int price = productHistory.getPrice();

        for (final Coupon coupon : productCoupons) {
            price = coupon.calculate(price);
        }

        productHistory.updatePrice(price);
    }

    public Cart getCart() {
        return cart;
    }
}
