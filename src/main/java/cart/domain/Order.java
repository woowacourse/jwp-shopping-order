package cart.domain;

import cart.exception.OrderException;

import java.sql.Timestamp;
import java.util.Objects;

public class Order {

    public static final int DISCOUNT_STANDARD_PRICE = 100000;
    public static final double DISCOUNT_RATIO = 0.1;
    public static final int FREE_DELIVERY_STANDARD_PRICE = 50000;
    public static final long FREE_DELIVERY_FEE = 0L;

    private Long id;
    private Member member;
    private OrderItems orderItems;
    private Long productPrice;
    private Long discountPrice;
    private Long deliveryFee;
    private Long totalPrice;
    private Timestamp orderTime;

    public Order(Member member, OrderItems orderItems, Long deliveryFee, Timestamp orderTime) {
        this.member = member;
        this.orderItems = orderItems;
        this.productPrice = orderItems.calculateOrderPrice();
        this.discountPrice = calculateDiscountedPrice();
        this.deliveryFee = calculateDeliveryFee(deliveryFee);
        this.totalPrice = calculateTotalPrice();
        this.orderTime = orderTime;
    }

    public Order(Long id, Member member, OrderItems orderItems, Long productPrice, Long discountPrice, Long deliveryFee, Long totalPrice, Timestamp orderTime) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
        this.orderTime = orderTime;
    }

    private Long calculateDiscountedPrice() {
        if (productPrice >= DISCOUNT_STANDARD_PRICE) {
            return (long) (productPrice * (1 - DISCOUNT_RATIO));
        }
        return productPrice;
    }

    private Long calculateDeliveryFee(Long deliveryFee) {
        if (productPrice >= FREE_DELIVERY_STANDARD_PRICE) {
            return FREE_DELIVERY_FEE;
        }
        return deliveryFee;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new OrderException.IllegalMember(this, member);
        }
    }

    private Long calculateTotalPrice() {
        return discountPrice + deliveryFee;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public Timestamp getOrderTime() {
        return orderTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(member, order.member) && Objects.equals(orderItems, order.orderItems) && Objects.equals(productPrice, order.productPrice) && Objects.equals(discountPrice, order.discountPrice) && Objects.equals(deliveryFee, order.deliveryFee) && Objects.equals(totalPrice, order.totalPrice) && Objects.equals(orderTime, order.orderTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, orderItems, productPrice, discountPrice, deliveryFee, totalPrice, orderTime);
    }
}
