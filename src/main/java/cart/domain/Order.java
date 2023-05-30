package cart.domain;

import cart.domain.value.Money;

import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;
    private Money deliveryFee;
    private Money purchaseItemPrice;
    private Money discountPurchaseItemPrice;

    public Order(
            final Long id,
            final Member member,
            final List<OrderItem> orderItems,
            final Money deliveryFee,
            final Money purchaseItemPrice,
            final Money discountPurchaseItemPrice
    ) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
        this.purchaseItemPrice = purchaseItemPrice;
        this.discountPurchaseItemPrice = discountPurchaseItemPrice;
    }

    public Order(final Member member, final List<OrderItem> orderItems) {
        this(null, member, orderItems, null, null, null);
    }

    public void calculateTotalPrinciplePrice() {
        int totalPrinciplePrice = orderItems.stream()
                .mapToInt(OrderItem::getPrinciplePrice)
                .sum();
        this.purchaseItemPrice = new Money(totalPrinciplePrice);
    }

    public int calculateTotalDiscountedPrice() {
        int discountedPrice = calculateItemDiscount() + calculateMemberDiscount();
        this.discountPurchaseItemPrice = new Money(discountedPrice);
        return discountedPrice;
    }

    private int calculateItemDiscount() {
        return orderItems.stream()
                .filter(OrderItem::isDiscount)
                .mapToInt(OrderItem::getItemDiscountedPrice)
                .sum();
    }

    private int calculateMemberDiscount() {
        return orderItems.stream()
                .filter(OrderItem::isNotDiscount)
                .mapToInt(orderitem -> orderitem.getMemberDiscountedPrice(member))
                .sum();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getDeliveryFee() {
        return deliveryFee.getMoney();
    }

    public int getPurchaseItemPrice() {
        return purchaseItemPrice.getMoney();
    }

    public int getDiscountPurchaseItemPrice() {
        return discountPurchaseItemPrice.getMoney();
    }
}
