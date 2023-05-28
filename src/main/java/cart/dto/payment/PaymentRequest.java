package cart.dto.payment;

import cart.dto.coupon.CouponIdRequest;
import cart.dto.product.ProductIdResponse;

import java.util.List;

public class PaymentRequest {

    private List<ProductIdResponse> products;
    private List<CouponIdRequest> coupons;

    public PaymentRequest() {
    }

    public PaymentRequest(final List<ProductIdResponse> products, final List<CouponIdRequest> coupons) {
        this.products = products;
        this.coupons = coupons;
    }

    public List<ProductIdResponse> getProducts() {
        return products;
    }

    public List<CouponIdRequest> getCoupons() {
        return coupons;
    }
}
