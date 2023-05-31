package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

public abstract class CartItemFixture {

    public static CartItem 장바구니_상품(Long 장바구니_상품_식별자값, Product 상품, Member 회원, int 상품_수량) {
        return new CartItem(장바구니_상품_식별자값, 상품, 회원, 상품_수량);
    }

    public static CartItem 장바구니_상품(Product 상품, Member 회원, int 상품_수량) {
        return new CartItem(상품, 회원, 상품_수량);
    }
}
