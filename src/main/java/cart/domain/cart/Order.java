package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.ProductHistory;
import cart.dto.coupon.CouponResponse;
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
        List<ProductHistory> productHistories = cart.buy(productIds, quantities);

        member.validateCoupons(couponsId);
        List<Coupon> coupons = member.getCouponsByIds(couponsId);

        int deliveryFee = cart.calculateDeliveryFeeUsingCoupons(coupons);

        for (ProductHistory productHistory : productHistories) {
            int price = productHistory.getPrice();
            for (Coupon coupon : coupons) {
                if(coupon.isDeliveryCoupon()) {
                    continue;
                }
                price = coupon.calculate(price);
            }
            productHistory.setPrice(price);
        }

        /////////

        List<ProductHistoryResponse> productsResponse = productHistories.stream()
                .map(ProductHistoryResponse::from)
                .collect(Collectors.toList());

        List<CouponResponse> couponsResponse = coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(timestamp.toString(), productsResponse, DeliveryFeeResponse.from(deliveryFee), couponsResponse);

    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Member getMember() {
        return member;
    }

    public Cart getCart() {
        return cart;
    }
}
