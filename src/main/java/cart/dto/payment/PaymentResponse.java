package cart.dto.payment;

import cart.domain.cart.Cart;
import cart.domain.member.Member;
import cart.dto.coupon.CouponResponse;
import cart.dto.product.DeliveryFeeResponse;
import cart.dto.product.ProductPayResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentResponse {

    private final List<ProductPayResponse> products;
    private final DeliveryFeeResponse price;
    private final List<CouponResponse> coupons;

    private PaymentResponse(final List<ProductPayResponse> products, final DeliveryFeeResponse price, final List<CouponResponse> coupons) {
        this.products = products;
        this.price = price;
        this.coupons = coupons;
    }

    public static PaymentResponse from(final Member member, final Cart cart) {
        List<ProductPayResponse> productPayResponses = cart.getCartItems().stream()
                .map(cartItem -> ProductPayResponse.from(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toList());

        DeliveryFeeResponse deliveryPrice = DeliveryFeeResponse.from(cart.getDeliveryFee());

        List<CouponResponse> coupons = member.getCoupons().stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        return new PaymentResponse(productPayResponses, deliveryPrice, coupons);
    }

    public List<ProductPayResponse> getProducts() {
        return products;
    }

    public DeliveryFeeResponse getDeliveryPrice() {
        return price;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
