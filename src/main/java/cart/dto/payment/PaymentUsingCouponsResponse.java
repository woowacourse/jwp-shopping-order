package cart.dto.payment;

import cart.domain.member.Member;
import cart.dto.product.DeliveryPayResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentUsingCouponsResponse {

    private final List<PaymentUsingCouponResponse> products;
    private final DeliveryPayResponse deliveryPrice;

    public PaymentUsingCouponsResponse(final List<PaymentUsingCouponResponse> products, final DeliveryPayResponse deliveryPrice) {
        this.products = products;
        this.deliveryPrice = deliveryPrice;
    }

    public static PaymentUsingCouponsResponse from(final Member member) {
        List<PaymentUsingCouponResponse> products = member.getCart().getCartItems().stream()
                .map(PaymentUsingCouponResponse::from)
                .collect(Collectors.toList());

        DeliveryPayResponse deliveryPayResponse = DeliveryPayResponse.from(member);

        return new PaymentUsingCouponsResponse(products, deliveryPayResponse);
    }

    public List<PaymentUsingCouponResponse> getProducts() {
        return products;
    }

    public DeliveryPayResponse getDeliveryPrice() {
        return deliveryPrice;
    }
}
