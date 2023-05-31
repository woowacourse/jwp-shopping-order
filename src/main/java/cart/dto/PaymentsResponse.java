package cart.dto;

import cart.domain.Price;

import java.util.List;

public class PaymentsResponse {
    private final List<ProductResponse> products;
    private final List<CouponResponse> coupons;
    private final int deliveryPrice;

    public PaymentsResponse(List<ProductResponse> products, List<CouponResponse> coupons, Price deliveryPrice) {
        this.products = products;
        this.coupons = coupons;
        this.deliveryPrice = deliveryPrice.getValue();
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
