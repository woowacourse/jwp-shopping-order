package cart.dto.payment;

import cart.domain.cart.Cart;
import cart.dto.coupon.CouponResponse;
import cart.dto.product.DeliveryPriceResponse;
import cart.dto.product.ProductPayResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentResponse {

    private final List<ProductPayResponse> products;
    private final DeliveryPriceResponse deliveryPrice;
    private final List<CouponResponse> coupons;

    private PaymentResponse(final List<ProductPayResponse> products, final DeliveryPriceResponse deliveryPrice, final List<CouponResponse> coupons) {
        this.products = products;
        this.deliveryPrice = deliveryPrice;
        this.coupons = coupons;
    }

    public static PaymentResponse from(final Cart cart) {
        List<ProductPayResponse> productPayResponses = cart.getCartItems().stream()
                .map(cartItem -> ProductPayResponse.from(cartItem.getProduct()))
                .collect(Collectors.toList());

        DeliveryPriceResponse deliveryPrice = new DeliveryPriceResponse(cart.getDeliveryFee());

        List<CouponResponse> coupons = cart.getMember().getCoupons().stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        return new PaymentResponse(productPayResponses, deliveryPrice, coupons);
    }

    public List<ProductPayResponse> getProducts() {
        return products;
    }

    public DeliveryPriceResponse getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
