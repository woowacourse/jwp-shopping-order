package cart.dto.payment;

import cart.dto.product.DeliveryPrice;

import java.util.List;

public class PaymentUsingCouponResponse {

    private final List<PaymentUsingCouponResponse> products;
    private final DeliveryPrice deliveryPrice;

    public PaymentUsingCouponResponse(final List<PaymentUsingCouponResponse> products, final DeliveryPrice deliveryPrice) {
        this.products = products;
        this.deliveryPrice = deliveryPrice;
    }

    public List<PaymentUsingCouponResponse> getProducts() {
        return products;
    }

    public DeliveryPrice getDeliveryPrice() {
        return deliveryPrice;
    }
}
