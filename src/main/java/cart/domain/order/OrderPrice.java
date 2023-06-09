package cart.domain.order;

import cart.domain.delivery.DeliveryPolicy;
import cart.domain.discount.DiscountPolicy;

public class OrderPrice {

    private Long productPrice;
    private Long discountPrice;
    private Long deliveryFee;
    private Long totalPrice;

    private OrderPrice(final Long productPrice, final Long discountPrice, final Long deliveryFee,
        final Long totalPrice) {
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
    }

    public static OrderPrice of(final Long productPrice, final Long discountPrice,
        final Long deliveryFee, final Long totalPrice) {
        return new OrderPrice(productPrice, discountPrice, deliveryFee, totalPrice);
    }

    public static OrderPrice of(final Long productPrice, final DiscountPolicy discountPolicy,
        final DeliveryPolicy deliveryPolicy) {
        final Long discountPrice = discountPolicy.calculate(productPrice);
        final Long deliveryFee = deliveryPolicy.getDeliveryFee(productPrice);
        return new OrderPrice(productPrice, discountPrice, deliveryFee, productPrice - discountPrice + deliveryFee);
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}
