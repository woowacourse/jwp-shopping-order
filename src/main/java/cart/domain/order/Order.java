package cart.domain.order;

import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.payment.Payment;
import cart.domain.vo.DeliveryFee;
import cart.domain.vo.Point;
import cart.domain.vo.Price;

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

    public static Order orderProductsAndUpdatePayment(Member member, List<CartItem> cartItems, int usedPoint, int deliveryFee) {
        OrderProducts orderProducts = OrderProducts.of(cartItems);
        Price totalPriceOfOrderProducts = new Price(orderProducts.calculateTotalPrice());
        Payment payment = Payment.of(totalPriceOfOrderProducts, new DeliveryFee(deliveryFee), new Point(usedPoint));
        return new Order(member, orderProducts, payment);
    }

    public static Order of(long id, Member member, OrderProducts orderProducts, Payment payment, LocalDateTime createdAt) {
        return new Order(id, member, orderProducts, payment, createdAt);
    }

    public int getUsedPointValue() {
        return payment.getUsedPointValue();
    }

    public int getTotalPriceValue() {
        return payment.getTotalPriceValue();
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

    public Point getUsedPoint() {
        return payment.getUsedPoint();
    }

    public int getUserPayment() {
        return payment.getUserPayment().getCash();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts.getOrderProducts();
    }

    public Payment getPayment() {
        return payment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
