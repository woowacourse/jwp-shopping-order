package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;

    private MemberDao memberDao;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        final Member member = memberDao.save(new Member("pizza1@pizza.com", "password1"));
        final Product product = productDao.save(new Product("치즈피자1", "1.jpg", 8900L));
        final CartItem cartItem = new CartItem(member, product);

        // when
        final CartItem result = cartItemDao.save(cartItem);

        // then
        final List<CartItem> cartItems = cartItemDao.findAllByMemberId(member.getId());
        assertAll(
                () -> assertThat(cartItems).hasSize(1),
                () -> assertThat(result.getQuantity()).isEqualTo(cartItem.getQuantity())
        );
    }

    @Test
    void 사용자_아이디를_받아_해당_사용자의_장바구니에_있는_모든_품목을_반환한다() {
        // given
        final Member member1 = memberDao.save(new Member("pizza1@pizza.com", "password1"));
        final Member member2 = memberDao.save(new Member("pizza2@pizza.com", "password2"));
        final Product product1 = productDao.save(new Product("치즈피자1", "1.jpg", 8900L));
        final Product product2 = productDao.save(new Product("치즈피자2", "2.jpg", 18900L));
        final Product product3 = productDao.save(new Product("치즈피자3", "3.jpg", 18900L));

        cartItemDao.save(new CartItem(member1, product1));
        cartItemDao.save(new CartItem(member1, product2));
        cartItemDao.save(new CartItem(member2, product3));

        // when
        final List<CartItem> result = cartItemDao.findAllByMemberId(member1.getId());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 사용자_아이디와_삭제할_장바구니의_상품_아이디를_받아_장바구니_항목을_제거한다() {
        // given
        final Member member = memberDao.save(new Member("pizza1@pizza.com", "password1"));
        final Product product = productDao.save(new Product("치즈피자1", "1.jpg", 8900L));
        final CartItem cartItem = cartItemDao.save(new CartItem(member, product));

        // when
        final int result = cartItemDao.delete(cartItem.getId(), member.getId());

        // then
        assertAll(
                () -> assertThat(cartItemDao.findAllByMemberId(member.getId())).isEmpty(),
                () -> assertThat(result).isOne()
        );
    }

    @Test
    void 장바구니의_상품의_수량을_변경한다() {
        // given
        final Member member = memberDao.save(new Member("pizza1@pizza.com", "password1"));
        final Product product = productDao.save(new Product("치즈피자1", "1.jpg", 8900L));
        final CartItem cartItem = cartItemDao.save(new CartItem(member, product));

        final CartItem updatedCartItem = new CartItem(cartItem.getId(), 2, cartItem.getMember(), cartItem.getProduct());

        // when
        cartItemDao.updateQuantity(updatedCartItem);

        // then
        final CartItem result = cartItemDao.findById(updatedCartItem.getId()).get();
        assertThat(result.getQuantity()).isEqualTo(2);
    }
}
