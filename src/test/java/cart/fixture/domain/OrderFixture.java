package cart.fixture.domain;

import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.vo.Money;

public abstract class OrderFixture {

    public static Order 주문(Member 회원, CartItems 장바구니_상품_목록, Money 사용_포인트) {
        return new Order(회원, 장바구니_상품_목록, 사용_포인트);
    }
}
