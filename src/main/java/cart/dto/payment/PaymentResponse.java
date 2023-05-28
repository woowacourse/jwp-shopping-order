package cart.dto.payment;

import cart.dto.coupon.CouponResponse;
import cart.dto.product.DeliveryPrice;
import cart.dto.product.ProductPayResponse;

import java.util.List;

public class PaymentResponse {

    private final List<ProductPayResponse> products;
    private final DeliveryPrice deliveryPrice;
    private final List<CouponResponse> coupons;

    public PaymentResponse(final List<ProductPayResponse> products, final DeliveryPrice deliveryPrice, final List<CouponResponse> coupons) {
        this.products = products;
        this.deliveryPrice = deliveryPrice;
        this.coupons = coupons;
    }

    public List<ProductPayResponse> getProducts() {
        return products;
    }

    public DeliveryPrice getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
