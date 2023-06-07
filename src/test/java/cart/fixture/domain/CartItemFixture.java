package cart.fixture.domain;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;

public abstract class CartItemFixture {

    public static CartItem 장바구니_상품(Long 장바구니_상품_식별자값, Product 상품, Member 회원, int 상품_수량) {
        return new CartItem(장바구니_상품_식별자값, 상품, 회원, 상품_수량);
    }

    public static CartItem 장바구니_상품(Product 상품, Member 회원, int 상품_수량) {
        return new CartItem(상품, 회원, 상품_수량);
    }
}
