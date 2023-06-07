package cart.dto.payment;

import cart.dto.coupon.CouponIdRequest;
import cart.dto.product.ProductIdRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PaymentRequest {

    @NotNull(message = "상품의 아이디를 입력해주세요.")
    private List<ProductIdRequest> products;

    @NotNull(message = "쿠폰의 아이디를 입력해주세요.")
    private List<CouponIdRequest> coupons;

    private PaymentRequest() {
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
