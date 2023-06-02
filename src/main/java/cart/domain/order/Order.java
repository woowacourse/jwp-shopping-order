package cart.domain.order;

import cart.domain.bill.Bill;
import cart.domain.member.Member;
import cart.domain.value.Money;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private static final Money BASE_SHIPPING_FEE = new Money(3000);
    private static final Money FREE = new Money(0);
    public static final Money MINIMUM_FREE_SHIPPING_STANDARD = new Money(50_000);

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final LocalDateTime generateTime;


    public Order(
            final Long id,
            final Member member,
            final OrderItems orderItems
    ) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.generateTime = LocalDateTime.now();
    }

    public Order(final Member member, final List<OrderItem> orderItems) {
        this(null, member, new OrderItems(orderItems));
    }


    public Bill makeBill() {
        Money totalDiscountedPrice = orderItems.getTotalDiscountedPrice(member);
        Money shippingFee = determineShippingFee(totalDiscountedPrice);
        return new Bill(
                orderItems.getItemBenefit(),
                orderItems.getMemberBenefit(member),
                orderItems.getTotalPrinciplePrice(),
                totalDiscountedPrice,
                shippingFee,
                totalDiscountedPrice.plus(shippingFee));
    }

    private Money determineShippingFee(final Money purchasePrice) {
        if (purchasePrice.isOver(MINIMUM_FREE_SHIPPING_STANDARD)) {
            return FREE;
        }
        return BASE_SHIPPING_FEE;
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

    public LocalDateTime getGenerateTime() {
        return generateTime;
    }
}
