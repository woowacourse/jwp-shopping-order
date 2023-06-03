package shop.application.order.dto;

import shop.domain.order.OrderPrice;

public class OrderPriceDto {
    private final long totalPrice;
    private final int deliveryPrice;
    private final long discountedPrice;

    private OrderPriceDto(long totalPrice, int deliveryPrice, long discountedPrice) {
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.discountedPrice = discountedPrice;
    }

    public static OrderPriceDto of(OrderPrice orderPrice) {
        return new OrderPriceDto(
                orderPrice.getTotalPrice(),
                orderPrice.getDeliveryPrice(),
                orderPrice.getDiscountedPrice()
        );
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public long getDiscountedPrice() {
        return discountedPrice;
    }
}
