package cart.fixture.entity;

import cart.dao.entity.CartItemEntity;

public abstract class CartItemEntityFixture {

    public static CartItemEntity 장바구니_상품_엔티티(Long 상품_식별자값, Long 회원_식별자값, int 상품_수량) {
        return new CartItemEntity(상품_식별자값, 회원_식별자값, 상품_수량);
    }
}
