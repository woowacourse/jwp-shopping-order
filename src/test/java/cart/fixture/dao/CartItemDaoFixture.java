package cart.fixture.dao;

import cart.dao.CartItemDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;

import java.util.Optional;

import static cart.fixture.entity.CartItemEntityFixture.장바구니_상품_엔티티;

public class CartItemDaoFixture {

    private final CartItemDao cartItemDao;

    public CartItemDaoFixture(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public CartItem 장바구니_상품을_등록한다(Product 상품, Member 회원, int 상품_수량) {
        CartItemEntity 장바구니_상품_엔티티 = 장바구니_상품_엔티티(상품.getId(), 회원.getId(), 상품_수량);
        Long 장바구니_상품_식별자값 = cartItemDao.insertCartItem(장바구니_상품_엔티티);

        return new CartItem(장바구니_상품_식별자값, 상품, 회원, 상품_수량);
    }

    public Optional<CartItem> 장바구니_상품을_조회한다(Long 장바구니_상품_식별자값) {
        return cartItemDao.findByCartItemId(장바구니_상품_식별자값);
    }
}
