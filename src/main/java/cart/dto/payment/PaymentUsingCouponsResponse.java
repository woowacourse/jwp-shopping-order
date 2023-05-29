package cart.dto.payment;

import cart.domain.cart.Cart;
import cart.domain.coupon.Coupon;
import cart.dto.product.DeliveryPayResponse;
import cart.dto.product.ProductUsingCouponAndSaleResponse;

import java.util.List;

public class PaymentUsingCouponsResponse {

    private final List<ProductUsingCouponAndSaleResponse> products;
    private final DeliveryPayResponse deliveryPrice;

    public PaymentUsingCouponsResponse(final List<ProductUsingCouponAndSaleResponse> products, final DeliveryPayResponse deliveryPrice) {
        this.products = products;
        this.deliveryPrice = deliveryPrice;
    }

    public static PaymentUsingCouponsResponse from(final Cart cart, final List<Coupon> requestCoupons) {
        int deliveryFee = cart.getDeliveryFee();
        int deliveryFeeUsingCoupons = deliveryFee - cart.calculateDeliveryFeeUsingCoupons(requestCoupons);
        DeliveryPayResponse deliveryPayResponse = new DeliveryPayResponse(deliveryFee, deliveryFeeUsingCoupons);

        return new PaymentUsingCouponsResponse(
                cart.getProductUsingCouponAndSaleResponse(requestCoupons),
                deliveryPayResponse
        );
    }

    public List<ProductUsingCouponAndSaleResponse> getProducts() {
        return products;
    }

    public DeliveryPayResponse getDeliveryPrice() {
        return deliveryPrice;
    }
}
