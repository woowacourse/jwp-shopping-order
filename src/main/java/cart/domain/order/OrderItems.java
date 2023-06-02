package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.value.Money;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;

    public OrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Money getTotalPrinciplePrice() {
        int totalPrinciplePrice = orderItems.stream()
                .map(OrderItem::getPrinciplePrice)
                .mapToInt(Money::getMoney)
                .sum();
        return new Money(totalPrinciplePrice);
    }

    public Money getTotalDiscountedPrice(final Member member) {
        int discountedPrice = calculateItemDiscount() + calculateMemberDiscount(member);
        return new Money(discountedPrice);
    }

    public Money getItemBenefit() {
        int discountedItemPrinciplePrice = orderItems.stream()
                .filter(OrderItem::isDiscount)
                .map(OrderItem::getPrinciplePrice)
                .mapToInt(Money::getMoney)
                .sum();
        return new Money(discountedItemPrinciplePrice - calculateItemDiscount());
    }

    public Money getMemberBenefit(final Member member) {
        int discountedItemPrinciplePrice = orderItems.stream()
                .filter(OrderItem::isNotDiscount)
                .map(OrderItem::getPrinciplePrice)
                .mapToInt(Money::getMoney)
                .sum();

        return new Money(discountedItemPrinciplePrice - calculateMemberDiscount(member));
    }

    private int calculateItemDiscount() {
        return orderItems.stream()
                .filter(OrderItem::isDiscount)
                .map(OrderItem::getItemDiscountedPrice)
                .mapToInt(Money::getMoney)
                .sum();
    }

    private int calculateMemberDiscount(final Member member) {
        return orderItems.stream()
                .filter(OrderItem::isNotDiscount)
                .map(orderitem -> orderitem.getMemberDiscountedPrice(member))
                .mapToInt(Money::getMoney)
                .sum();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
