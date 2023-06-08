package cart.domain;

import cart.exception.OrderException;

import java.sql.Timestamp;
import java.util.Objects;

public class Order {

    private static final int DISCOUNT_STANDARD_PRICE = 100000;
    private static final double DISCOUNT_RATIO = 0.1;
    private static final int FREE_DELIVERY_STANDARD_PRICE = 50000;
    private static final long FREE_DELIVERY_FEE = 0L;
    private static final long DELIVERY_FEE = 3000L;

    private Long id;
    private Member member;
    private OrderItems orderItems;
    private Long productPrice;
    private Long discountPrice;
    private Long deliveryFee;
    private Long totalPrice;
    private Timestamp orderTime;
    private Order(Long id, Member member, OrderItems orderItems, Long productPrice, Long discountPrice, Long deliveryFee, Long totalPrice, Timestamp orderTime) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
        this.orderTime = orderTime;
    }

    public static Order createInitOrder(Member member, OrderItems orderItems, Timestamp orderTime) {
        Long productPrice = orderItems.calculateOrderPrice();
        Long discountedPrice = calculateDiscountedPrice(productPrice);
        Long deliveryFee = calculateDeliveryFee(productPrice);
        Long totalPrice = calculateTotalPrice(discountedPrice, deliveryFee);

        return new Order(null, member, orderItems, productPrice, discountedPrice, deliveryFee, totalPrice, orderTime);
    }

    public static Order of(Long id, Member member, OrderItems orderItems, Long productPrice, Long discountPrice, Long deliveryFee, Long totalPrice, Timestamp orderTime) {
        return new Order(id, member, orderItems, productPrice, discountPrice, deliveryFee, totalPrice, orderTime);
    }

    private static Long calculateDiscountedPrice(Long productPrice) {
        if (productPrice >= DISCOUNT_STANDARD_PRICE) {
            return (long) (productPrice * (1 - DISCOUNT_RATIO));
        }
        return productPrice;
    }

    private static Long calculateDeliveryFee(Long productPrice) {
        if (productPrice >= FREE_DELIVERY_STANDARD_PRICE) {
            return FREE_DELIVERY_FEE;
        }
        return DELIVERY_FEE;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new OrderException.IllegalMember(this, member);
        }
    }

    private static Long calculateTotalPrice(Long discountPrice, Long deliveryFee) {
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
