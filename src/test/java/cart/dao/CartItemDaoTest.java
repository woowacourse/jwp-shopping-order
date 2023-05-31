package cart.dao;

import cart.domain.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static cart.ShoppingOrderFixture.member1;
import static cart.ShoppingOrderFixture.pizza;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@JdbcTest
class CartItemDaoTest {

    private static final CartItem ADDED_CART_ITEM = new CartItem(member1, pizza);

    private CartItemDao cartItemDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @DisplayName("findById 테스트")
    @Test
    void findByIdTest() {
        Long cartItemId = cartItemDao.save(ADDED_CART_ITEM);
        CartItem cartItem = cartItemDao.findById(cartItemId);

        assertSoftly(softly -> {
            softly.assertThat(cartItem.getId()).isEqualTo(cartItemId);
            softly.assertThat(cartItem.getProduct()).isEqualTo(ADDED_CART_ITEM.getProduct());
            softly.assertThat(cartItem.getMember()).isEqualTo(ADDED_CART_ITEM.getMember());
            softly.assertThat(cartItem.getQuantity()).isEqualTo(ADDED_CART_ITEM.getQuantity());
        });
    }

    @DisplayName("findByMemberId 테스트")
    @Test
    void findByMemberIdTest() {
        // data.sql 파일에서 member_id 1에 2개의 cart_item을 사전에 추가
        List<CartItem> cartItems = cartItemDao.findByMemberId(1L);
        assertThat(cartItems).hasSize(2);
    }

    @DisplayName("updateQuantity 테스트")
    @Test
    void updateQuantityTest() {
        Long productId = cartItemDao.save(ADDED_CART_ITEM);
        CartItem beforeCartItem = cartItemDao.findById(productId);

        int affectedRow = cartItemDao.updateQuantity(new CartItem(productId, 3L, pizza, member1));
        CartItem afterCartItem = cartItemDao.findById(productId);

        assertSoftly(softly -> {
            softly.assertThat(affectedRow).isEqualTo(1);
            softly.assertThat(beforeCartItem.getMember()).isEqualTo(afterCartItem.getMember());
            softly.assertThat(beforeCartItem.getProduct()).isEqualTo(afterCartItem.getProduct());
            softly.assertThat(beforeCartItem.getQuantity()).isEqualTo(ADDED_CART_ITEM.getQuantity());
            softly.assertThat(afterCartItem.getQuantity()).isEqualTo(3);
        });
    }

    @DisplayName("delete 테스트")
    @Test
    void deleteTest() {
        cartItemDao.save(ADDED_CART_ITEM);
        int affectedRow = cartItemDao.delete(ADDED_CART_ITEM.getMember().getId(), ADDED_CART_ITEM.getProduct().getId());

        assertSoftly(softly -> {
            softly.assertThat(affectedRow).isGreaterThanOrEqualTo(1);
            assertThat(cartItemDao.findByMemberId(1L)).doesNotContain(ADDED_CART_ITEM);
        });
    }

    @DisplayName("deleteByMemberId 테스트")
    @Test
    void deleteByMemberIdTest() {
        cartItemDao.save(ADDED_CART_ITEM);
        int affectedRow = cartItemDao.deleteByMemberId(ADDED_CART_ITEM.getMember().getId());

        assertSoftly(softly -> {
            softly.assertThat(affectedRow).isGreaterThanOrEqualTo(1);
            assertThat(cartItemDao.findByMemberId(ADDED_CART_ITEM.getMember().getId())).isEmpty();
        });
    }

    @DisplayName("deleteById 테스트")
    @Test
    void deleteByIdTest() {
        Long cartItemId = cartItemDao.save(ADDED_CART_ITEM);
        int affectedRow = cartItemDao.deleteById(cartItemId);

        assertSoftly(softly -> {
            softly.assertThat(affectedRow).isGreaterThanOrEqualTo(1);
            assertThatThrownBy(() -> cartItemDao.findById(cartItemId))
                    .isInstanceOf(DataAccessException.class);
        });
    }
}
