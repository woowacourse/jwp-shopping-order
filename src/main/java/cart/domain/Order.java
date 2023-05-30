package cart.domain;

import cart.domain.value.Money;

import java.util.List;

public class Order {

    private static final int BASE_SHIPPING_FEE = 3000;
    private static final int FREE = 0;
    public static final int MINIMUM_FREE_SHIPPING_STANDARD = 50_000;

    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;
    private Money shippingFee;
    private Money purchaseItemPrice;
    private Money discountPurchaseItemPrice;

    public Order(
            final Long id,
            final Member member,
            final List<OrderItem> orderItems,
            final Money shippingFee,
            final Money purchaseItemPrice,
            final Money discountPurchaseItemPrice
    ) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.shippingFee = shippingFee;
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

    public void determineShippingFee(final int purchasePrice) {
        if (purchasePrice >= MINIMUM_FREE_SHIPPING_STANDARD) {
            this.shippingFee = new Money(FREE);
            return;
        }
        this.shippingFee = new Money(BASE_SHIPPING_FEE);
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

    public int getShippingFee() {
        return shippingFee.getMoney();
    }

    public int getPurchaseItemPrice() {
        return purchaseItemPrice.getMoney();
    }

    public int getDiscountPurchaseItemPrice() {
        return discountPurchaseItemPrice.getMoney();
    }
}
