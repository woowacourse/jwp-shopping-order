package cart.domain.order;

import cart.domain.CartItems;
import cart.domain.Member;

public class Order {

    private final Long id;
    private final Member member;
    private final CartItems cartItems;
    private final DiscountPolicy discountPolicy;

    public Order(final Member member, final CartItems cartItems, final DiscountPolicy discountPolicy) {
        this(null, member, cartItems, discountPolicy);
    }

    public Order(final Long id, final Member member, final CartItems cartItems, final DiscountPolicy discountPolicy) {
        validateMember(member, cartItems);
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.discountPolicy = discountPolicy;
    }

    private void validateMember(final Member member, final CartItems cartItems) {
        if (!member.equals(cartItems.getMember())) {
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
        return cartItems.sumOfPrice();
    }

    public Price getDiscountedPrice() {
        final Price originalPrice = cartItems.sumOfPrice();
        return discountPolicy.discount(originalPrice);
    }

}
