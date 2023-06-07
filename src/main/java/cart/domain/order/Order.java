package cart.domain.order;

import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.pay.PointPolicy;
import cart.domain.vo.Money;
import cart.exception.OrderException;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Member member;
    private final CartItems cartItems;
    private final Money totalPrice;
    private final Money usePoint;
    private final Money deliveryFee;
    private final LocalDateTime createdAt;

    public Order(Member member, CartItems cartItems, Money usePoint, Money deliveryFee) {
        this(null, member, cartItems, usePoint, deliveryFee, LocalDateTime.now());
    }

    public Order(Long id, Member member, CartItems cartItems, Money usePoint, Money deliveryFee, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.totalPrice = cartItems.totalPrice();
        this.usePoint = usePoint;
        this.deliveryFee = deliveryFee;
        this.createdAt = createdAt;
    }

    public static Order of(Member member, CartItems cartItems, Money deliveryFee, Money usePoint, PointPolicy pointPolicy) {
        cartItems.checkOwner(member);
        validateOverFlowPoint(cartItems, usePoint);

        Money payWithOutDeliveryFee = cartItems.totalPrice().minus(usePoint);
        Money realPay = payWithOutDeliveryFee.plus(deliveryFee);

        member.spendPoint(usePoint);
        member.spendMoney(realPay);
        member.accumulatePoint(pointPolicy.calculate(payWithOutDeliveryFee));

        return new Order(member, cartItems, usePoint, deliveryFee);
    }

    private static void validateOverFlowPoint(CartItems cartItems, Money usePoint) {
        if (usePoint.isGreaterThan(cartItems.totalPrice())) {
            throw new OrderException.OverFlowPoint();
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public CartItems getCartItems() {
        return cartItems;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    public Money getUsePoint() {
        return usePoint;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(member, order.member) && Objects.equals(cartItems, order.cartItems) && Objects.equals(totalPrice, order.totalPrice) && Objects.equals(usePoint, order.usePoint) && Objects.equals(deliveryFee, order.deliveryFee) && Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, cartItems, totalPrice, usePoint, deliveryFee, createdAt);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", cartItems=" + cartItems +
                ", totalPrice=" + totalPrice +
                ", usePoint=" + usePoint +
                ", deliveryFee=" + deliveryFee +
                ", createdAt=" + createdAt +
                '}';
    }
}
