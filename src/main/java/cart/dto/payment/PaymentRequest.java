package cart.dto.payment;

import cart.dto.coupon.CouponIdRequest;
import cart.dto.product.ProductIdRequest;

import java.util.List;

public class PaymentRequest {

    private List<ProductIdRequest> products;
    private List<CouponIdRequest> coupons;

    public PaymentRequest() {
    }

    public PaymentRequest(final List<ProductIdRequest> products, final List<CouponIdRequest> coupons) {
        this.products = products;
        this.coupons = coupons;
    }

    public List<ProductIdRequest> getProducts() {
        return products;
    }

    public List<CouponIdRequest> getCoupons() {
        return coupons;
    }
}
