package shop.domain.order;

import shop.domain.coupon.Coupon;

import java.util.List;

public class OrderPrice {
    private final static int DEFAULT_DELIVERY_PRICE = 3000;

    private final long orderPrice;
    private final int deliveryPrice;

    private OrderPrice(long orderPrice) {
        this.orderPrice = orderPrice;
        this.deliveryPrice = DEFAULT_DELIVERY_PRICE;
    }

    private OrderPrice(long orderPrice, int deliveryPrice) {
        this.orderPrice = orderPrice;
        this.deliveryPrice = deliveryPrice;
    }

    public static OrderPrice create(long orderPrice, int deliveryPrice) {
        return new OrderPrice(orderPrice, deliveryPrice);
    }

    public static OrderPrice createFromItems(List<OrderItem> orderItems) {
        long totalPrice = getTotalPrice(orderItems);

        return new OrderPrice(totalPrice);
    }

    public static OrderPrice createFromItemsWithCoupon(List<OrderItem> orderItems, Coupon coupon) {
        long totalPrice = getTotalPrice(orderItems);
        long discountedPrice = totalPrice * (100 - coupon.getDiscountRate()) / 100;

        return new OrderPrice(discountedPrice);
    }

    private static long getTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(OrderItem::getTotalPrice)
                .sum();
    }

    public long getOrderPrice() {
        return orderPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }
}
