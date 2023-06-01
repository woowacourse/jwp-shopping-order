package cart.domain.order;

import cart.domain.member.Member;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;

    public OrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int getTotalPrinciplePrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getPrinciplePrice)
                .sum();
    }

    public int getTotalDiscountedPrice(final Member member) {
        int discountedPrice = calculateItemDiscount() + calculateMemberDiscount(member);
        return discountedPrice;
    }

    public int getItemBenefit() {
        int discountedItemPrinciplePrice = orderItems.stream()
                .filter(OrderItem::isDiscount)
                .mapToInt(OrderItem::getPrinciplePrice)
                .sum();
        return discountedItemPrinciplePrice - calculateItemDiscount();
    }

    public int getMemberBenefit(final Member member) {
        int discountedItemPrinciplePrice = orderItems.stream()
                .filter(OrderItem::isNotDiscount)
                .mapToInt(OrderItem::getPrinciplePrice)
                .sum();

        return discountedItemPrinciplePrice - calculateMemberDiscount(member);
    }

    private int calculateItemDiscount() {
        return orderItems.stream()
                .filter(OrderItem::isDiscount)
                .mapToInt(OrderItem::getItemDiscountedPrice)
                .sum();
    }

    private int calculateMemberDiscount(final Member member) {
        return orderItems.stream()
                .filter(OrderItem::isNotDiscount)
                .mapToInt(orderitem -> orderitem.getMemberDiscountedPrice(member))
                .sum();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
