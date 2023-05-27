package cart.dao;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import cart.domain.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        this.cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("장바구니 상품 조회 시 멤버 ID와 상품 ID에 해당하는 장바구니 상품이 존재하지 않으면 빈 Optional을 반환한다.")
    void selectByMemberIdAndProductId_empty_Optional() {
        // given
        Long notExistMemberId = -1L;
        Long notExistsProductId = -1L;

        // when
        Optional<CartItem> cartItem = cartItemDao.selectByMemberIdAndProductId(notExistMemberId, notExistsProductId);

        // then
        assertThat(cartItem.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("장바구니 상품 조회 시  멤버 ID와 상품 ID에 해당하는 장바구니 상품이 존재하면 장바구니 상품을 반환한다.")
    void selectByMemberIdAndProductId() {
        // given
        Long memberId = Dooly_CartItem1.MEMBER.getId();
        Long productId = Dooly_CartItem1.PRODUCT.getId();

        // when
        Optional<CartItem> cartItem = cartItemDao.selectByMemberIdAndProductId(memberId, productId);

        // then
        assertThat(cartItem.get()).usingRecursiveComparison().isEqualTo(Dooly_CartItem1.ENTITY);
    }
}
