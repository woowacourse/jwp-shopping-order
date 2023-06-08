package cart.dao;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.notfound.CartItemNotFoundException;
import cart.exception.notfound.MemberNotFoundException;
import cart.exception.notfound.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Import({CartItemDao.class, ProductDao.class, MemberDao.class})
public class CartItemDaoTest {

    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MemberDao memberDao;


    @DisplayName("장바구니에 상품을 추가하고 가져올 수 있다.")
    @Test
    void saveAndGet() {
        // given
        final Member member = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);
        final Product product = productDao.findById(1L).orElseThrow(ProductNotFoundException::new);

        final CartItem cartItem = new CartItem(member, product, new Quantity(3));

        // when
        final Long cartItemId = cartItemDao.insert(cartItem);

        // then
        final CartItem findCartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        assertAll(
                () -> assertThat(findCartItem.getMember()).isEqualTo(member),
                () -> assertThat(findCartItem.getProduct()).isEqualTo(product),
                () -> assertThat(findCartItem.getQuantityValue()).isEqualTo(3)
        );
    }

    @DisplayName("사용자가 가지고 있는 장바구니 상품들을 가져올 수 있다.")
    @Test
    void findByMemberId() {
        // given
        final Member member = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);

        // when
        final List<CartItem> cartItems = cartItemDao.findAllByMemberId(member.getId());

        // then
        assertAll(
                () -> assertThat(cartItems).hasSize(6),
                () -> assertThat(cartItems.get(0).getProductId()).isEqualTo(1),
                () -> assertThat(cartItems.get(1).getProductId()).isEqualTo(2),
                () -> assertThat(cartItems.get(2).getProductId()).isEqualTo(2),
                () -> assertThat(cartItems.get(3).getProductId()).isEqualTo(3),
                () -> assertThat(cartItems.get(4).getProductId()).isEqualTo(4),
                () -> assertThat(cartItems.get(5).getProductId()).isEqualTo(5)
        );
    }

    @DisplayName("List<Long> cartItemId로 장바구니 상품들을 가져올 수 있다.")
    @Test
    void getCartItemsByIds() {
        // given, when
        final List<CartItem> cartItems = cartItemDao.findAllByIds(List.of(1L, 2L, 3L));

        // then
        assertAll(
                () -> assertThat(cartItems).hasSize(3),
                () -> assertThat(cartItems.get(0).getProductId()).isEqualTo(1),
                () -> assertThat(cartItems.get(1).getProductId()).isEqualTo(2),
                () -> assertThat(cartItems.get(2).getProductId()).isEqualTo(2)
        );
    }

    @DisplayName("장바구니 상품을 삭제할 수 있다.")
    @Test
    void deleteById() {
        // given
        final CartItem cartItem = cartItemDao.findById(1L).orElseThrow(CartItemNotFoundException::new);

        // when
        cartItemDao.delete(cartItem.getId());

        // then
        assertThat(cartItemDao.findById(cartItem.getId())).isEmpty();
    }

    @DisplayName("List<Long> ids에 해당하는 장바구니 상품을 모두 제거할 수 있다.")
    @Test
    void deleteByIds() {
        // given
        final List<Long> ids = List.of(1L, 2L);

        //when
        cartItemDao.deleteByIds(ids);

        // when, then
        assertThat(cartItemDao.findAllByIds(ids)).isEmpty();
    }

    @DisplayName("장바구니 상품의 수량을 변경할 수 있다.")
    @Test
    void updateQuantity() {
        // given
        final CartItem cartItem = cartItemDao.findById(1L).orElseThrow(CartItemNotFoundException::new);

        // when
        final CartItem updateCartItem = new CartItem(cartItem.getId(), cartItem.getMember(), cartItem.getProduct(), new Quantity(5));
        cartItemDao.update(updateCartItem);

        // then
        final CartItem result = cartItemDao.findById(1L).orElseThrow(CartItemNotFoundException::new);
        assertThat(result.getQuantity()).isEqualTo(new Quantity(5));
    }
}
