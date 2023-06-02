package cart.cartitem.repository;

import cart.cartitem.dao.CartItemDao;
import cart.cartitem.domain.CartItem;
import cart.init.DBInit;
import cart.member.dao.MemberDao;
import cart.product.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static cart.cartitem.domain.CartItemTest.CART_ITEM;
import static cart.member.domain.MemberTest.MEMBER;
import static cart.product.domain.ProductTest.PRODUCT_FIRST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CartItemRepositoryTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        final CartItemDao cartItemDao = new CartItemDao(jdbcTemplate);
        final ProductDao productDao = new ProductDao(jdbcTemplate);
        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        cartItemRepository = new CartItemRepository(cartItemDao, productDao, memberDao);
    }

    @Test
    void MemberId로_장바구니_조회하기() {
        // when
        final List<CartItem> cartItems = cartItemRepository.findByMemberId(1L);

        // then
        assertThat(cartItems).hasSize(3);
    }

    @Test
    void 장바구니를_저장한다() {
        // when
        final Long cartItemId = cartItemRepository.save(CART_ITEM);

        // then
        assertThat(cartItemId).isEqualTo(5L);
    }

    @Test
    void cartItemId로_장바구니를_조회한다() {
        // given
        final Long cartItemId = cartItemRepository.save(CART_ITEM);

        // when
        final CartItem cartItem = cartItemRepository.findById(cartItemId);

        // then
        assertThat(cartItem).isEqualTo(CART_ITEM);
    }

    @Test
    void cartItemId로_장바구니를_삭제한다() {
        // expect
        assertThatNoException()
                .isThrownBy(() -> cartItemRepository.removeById(1L));
    }

    @Test
    void 장바구니_수량을_변경한다() {
        // given
        final CartItem cartItem = new CartItem(1L, 100L, PRODUCT_FIRST, MEMBER);

        // when
        cartItemRepository.updateQuantity(cartItem);

        // then
        assertThat(cartItemRepository.findById(1L)).isEqualTo(cartItem);
    }
}
