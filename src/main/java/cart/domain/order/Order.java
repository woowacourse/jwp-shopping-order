package cart.domain.order;

import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.payment.Payment;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private final Member member;
    private final OrderProducts orderProducts;
    private final Payment payment;
    private LocalDateTime createdAt;

    private Order(Member member, OrderProducts orderProducts, Payment payment) {
        this.member = member;
        this.orderProducts = orderProducts;
        this.payment = payment;
    }

    private Order(long id, Member member, OrderProducts orderProducts, Payment payment, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.orderProducts = orderProducts;
        this.payment = payment;
        this.createdAt = createdAt;
    }

    public static Order of(Member member, List<CartItem> cartItems, int usedPoint, int deliveryFee) {
        OrderProducts orderProducts = OrderProducts.of(cartItems);
        Payment payment = new Payment(orderProducts.calculateTotalPrice() + deliveryFee, usedPoint);
        member.pay(payment);
        return new Order(member, orderProducts, payment);
    }

    public static Order of(long id, Member member, OrderProducts orderProducts, Payment payment, LocalDateTime createdAt) {
        return new Order(id, member, orderProducts, payment, createdAt);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public OrderProducts getOrderProducts() {
        return orderProducts;
    }

    public Payment getPayment() {
        return payment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", orderProducts=" + orderProducts +
                ", payment=" + payment +
                ", createdAt=" + createdAt +
                '}';
    }
}
