package cart.domain.order;

import cart.domain.Member;

public class Order {

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final DiscountPolicy discountPolicy;

    public Order(final Member member, final OrderItems orderItems, final DiscountPolicy discountPolicy) {
        this(null, member, orderItems, discountPolicy);
    }

    public Order(final Long id, final Member member, final OrderItems orderItems, final DiscountPolicy discountPolicy) {
        validateMember(member, orderItems);
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.discountPolicy = discountPolicy;
    }

    private void validateMember(final Member member, final OrderItems orderItems) {
        if (!member.equals(orderItems.getMember())) {
            throw new IllegalArgumentException("장바구니 상품을 추가한 Member와 주문을 생성한 Member가 일치하지 않습니다");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Price getOriginalPrice() {
        return orderItems.sumOfPrice();
    }

    public Price getDiscountedPrice() {
        final Price originalPrice = orderItems.sumOfPrice();
        return discountPolicy.discount(originalPrice);
    }

}
