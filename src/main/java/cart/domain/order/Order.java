package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.value.Money;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private static final int BASE_SHIPPING_FEE = 3000;
    private static final int FREE = 0;
    public static final int MINIMUM_FREE_SHIPPING_STANDARD = 50_000;

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final LocalDateTime generateTime;
    private Money shippingFee;
    private Money purchaseItemPrice;
    private Money discountPurchaseItemPrice;

    public Order(
            final Long id,
            final Member member,
            final OrderItems orderItems,
            final Money shippingFee,
            final Money purchaseItemPrice,
            final Money discountPurchaseItemPrice
    ) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.generateTime = LocalDateTime.now();
        this.shippingFee = shippingFee;
        this.purchaseItemPrice = purchaseItemPrice;
        this.discountPurchaseItemPrice = discountPurchaseItemPrice;
    }

    public Order(final Member member, final List<OrderItem> orderItems) {
        this(null, member, new OrderItems(orderItems), new Money(0), new Money(0), new Money(0));
    }

    public void calculatePrice() {
        purchaseItemPrice = new Money(orderItems.getTotalPrinciplePrice());
        discountPurchaseItemPrice = new Money(orderItems.getTotalDiscountedPrice(member));
        determineShippingFee(discountPurchaseItemPrice.getMoney());
    }


    public void determineShippingFee(final int purchasePrice) {
        if (purchasePrice >= MINIMUM_FREE_SHIPPING_STANDARD) {
            this.shippingFee = new Money(FREE);
            return;
        }
        this.shippingFee = new Money(BASE_SHIPPING_FEE);
    }

    public void validateBill(final int totalItemPrice,
                             final int discountedTotalItemPrice,
                             final int totalItemDiscountAmount,
                             final int totalMemberDiscountAmount,
                             final int shippingFee
    ) {
        if (!checkBill(totalItemPrice, discountedTotalItemPrice, totalItemDiscountAmount, totalMemberDiscountAmount, shippingFee)) {
            throw new IllegalArgumentException("주문 요청정보가 잘못되었습니다. 다시 요청보내주세요");
        }
    }

    private boolean checkBill(final int totalItemPrice,
                              final int discountedTotalItemPrice,
                              final int totalItemDiscountAmount,
                              final int totalMemberDiscountAmount,
                              final int shippingFee
    ) {
        return purchaseItemPrice.isSame(totalItemPrice)
                && discountPurchaseItemPrice.isSame(discountedTotalItemPrice)
                && this.shippingFee.isSame(shippingFee)
                && orderItems.getItemBenefit() == totalItemDiscountAmount
                && orderItems.getMemberBenefit(member) == totalMemberDiscountAmount;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems.getOrderItems();
    }

    public LocalDateTime getGenerateTime() {
        return generateTime;
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
