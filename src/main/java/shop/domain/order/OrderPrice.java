package shop.domain.order;

import shop.domain.coupon.Coupon;

import java.util.List;

public class OrderPrice {
    private final static int DEFAULT_DELIVERY_PRICE = 3000;

    private final long totalPrice;
    private final int deliveryPrice;
    private final long discountedPrice;

    private OrderPrice(long totalPrice, long discountedPrice) {
        this.totalPrice = totalPrice;
        this.deliveryPrice = DEFAULT_DELIVERY_PRICE;
        this.discountedPrice = discountedPrice;
    }

    private OrderPrice(long totalPrice, int deliveryPrice, long discountedPrice) {
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.discountedPrice = discountedPrice;
    }

    public static OrderPrice create(long orderPrice, int deliveryPrice, long discountedPrice) {
        return new OrderPrice(orderPrice, deliveryPrice, discountedPrice);
    }

    public static OrderPrice createFromItems(List<OrderItem> orderItems) {
        long totalPrice = getTotalPrice(orderItems);

        return new OrderPrice(totalPrice, 0);
    }

    public static OrderPrice createFromItemsWithCoupon(List<OrderItem> orderItems, Coupon coupon) {
        long totalPrice = getTotalPrice(orderItems);
        long discountedPrice = totalPrice * (100 - coupon.getDiscountRate()) / 100;

        return new OrderPrice(totalPrice, discountedPrice);
    }

    private static long getTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(OrderItem::getTotalPrice)
                .sum();
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
