package cart.domain;

import cart.exception.OrderException;

import java.sql.Timestamp;
import java.util.Objects;

public class Order {

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
        if (productPrice >= 100000) {
            return (long) (productPrice * 0.9);
        }
        return productPrice;
    }

    private Long calculateDeliveryFee(Long deliveryFee) {
        if (productPrice >= 50000) {
            return 0L;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
