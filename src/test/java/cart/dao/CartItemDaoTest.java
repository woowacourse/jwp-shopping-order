package cart.dao;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        final Product product = productDao.getProductById(1L);

        final CartItem cartItem = new CartItem(member, product, 3);

        // when
        final Long cartItemId = cartItemDao.save(cartItem);

        // then
        final CartItem findCartItem = cartItemDao.findById(cartItemId);
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
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());

        // then
        assertAll(
                () -> assertThat(cartItems).hasSize(5),
                () -> assertThat(cartItems.get(0).getProductId()).isEqualTo(1),
                () -> assertThat(cartItems.get(1).getProductId()).isEqualTo(2),
                () -> assertThat(cartItems.get(2).getProductId()).isEqualTo(2),
                () -> assertThat(cartItems.get(3).getProductId()).isEqualTo(3),
                () -> assertThat(cartItems.get(4).getProductId()).isEqualTo(4)
        );
    }

    @DisplayName("List<Long> cartItemId로 장바구니 상품들을 가져올 수 있다.")
    @Test
    void getCartItemsByIds() {
        // given, when
        final List<CartItem> cartItems = cartItemDao.getCartItemsByIds(List.of(1L, 2L, 3L));

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
        final CartItem cartItem = cartItemDao.findById(1L);

        // when
        cartItemDao.deleteById(cartItem.getId());

        // then
        assertThatThrownBy(() -> cartItemDao.findById(cartItem.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("장바구니 상품의 수량을 변경할 수 있다.")
    @Test
    void updateQuantity() {
        // given
        final CartItem cartItem = cartItemDao.findById(1L);

        // when
        final CartItem updateCartItem = new CartItem(cartItem.getId(), cartItem.getMember(), cartItem.getProduct(), 5);
        cartItemDao.updateQuantity(updateCartItem);

        // then
        final CartItem result = cartItemDao.findById(1L);
        assertThat(result.getQuantity()).isEqualTo(new Quantity(5));
    }
}
