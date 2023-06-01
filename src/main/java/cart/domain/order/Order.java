package cart.domain.order;

import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.pay.PayPoint;
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
    private final LocalDateTime createdAt;

    public Order(Member member, CartItems cartItems, Money usePoint) {
        this(null, member, cartItems, usePoint, LocalDateTime.now());
    }

    public Order(Long id, Member member, CartItems cartItems, Money usePoint, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.totalPrice = cartItems.totalPrice();
        this.usePoint = usePoint;
        this.createdAt = createdAt;
    }

    public static Order of(Member member, CartItems cartItems, Money usePoint, PayPoint payPoint) {
        Money currentTotalPrice = cartItems.totalPrice();
        validatePointAffordable(member, usePoint);
        validateMoneyAffordable(member, currentTotalPrice);

        member.spendMoney(currentTotalPrice);
        member.spendPoint(usePoint);

        Money totalPay = currentTotalPrice.minus(usePoint);
        member.accumulatePoint(payPoint.calculate(totalPay));

        return new Order(member, cartItems, usePoint);
    }

    private static void validatePointAffordable(Member member, Money usePoint) {
        if (!member.isPointAffordable(usePoint)) {
            throw new OrderException.NotEnoughPoint();
        }
    }

    private static void validateMoneyAffordable(Member member, Money totalPrice) {
        if (!member.isMoneyAffordable(totalPrice)) {
            throw new OrderException.NotEnoughMoney();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(member, order.member) && Objects.equals(cartItems, order.cartItems) && Objects.equals(totalPrice, order.totalPrice) && Objects.equals(usePoint, order.usePoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, cartItems, totalPrice, usePoint);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", cartItems=" + cartItems +
                ", totalPrice=" + totalPrice +
                ", usePoint=" + usePoint +
                '}';
    }
}
