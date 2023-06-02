package cart.domain;

import java.sql.Date;

public class Order {

    private Long id;
    private Member member;
    private OrderItems orderItems;
    private Long productPrice;
    private Long discountPrice;
    private Long deliveryFee;
    private Long totalPrice;
    private java.sql.Date date;


    public Order(Member member, OrderItems orderItems, Long deliveryFee, Date date) {
        this.member = member;
        this.orderItems = orderItems;
        this.productPrice = orderItems.calculateOrderPrice();
        this.discountPrice = calculateDiscountedPrice();
        this.deliveryFee = calculateDeliveryFee(deliveryFee);
        this.totalPrice = calculateTotalPrice();
        this.date = date;
    }

    public Order(Long id, Member member, OrderItems orderItems, Long productPrice, Long discountPrice, Long deliveryFee, Long totalPrice, Date date) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setOrderItems(OrderItems orderItems) {
        this.orderItems = orderItems;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void setDeliveryFee(Long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
