package cart.dao;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.MemberFixtures.Ber;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import cart.domain.cartitem.CartItem;
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
    @DisplayName("해당하는 장바구니 상품의 수량을 변경한다")
    void updateQuantity() {
        // given
        CartItem currentCartItem = Dooly_CartItem1.ENTITY;
        int currentQuantity = currentCartItem.getQuantity();
        int quantityToAdd = 5;
        currentCartItem.addQuantity(quantityToAdd);
        int addedQuantity = currentQuantity + quantityToAdd;

        // when
        cartItemDao.updateQuantity(currentCartItem);
        System.out.println("cartItemQ = " + currentCartItem.getQuantity());
        CartItem updatedCartItem = cartItemDao.findById(currentCartItem.getId());

        // then
        assertThat(updatedCartItem.getQuantity()).isEqualTo(addedQuantity);
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
    @DisplayName("장바구니 상품 조회 시 멤버 ID와 상품 ID에 해당하는 장바구니 상품이 존재하면 장바구니 상품을 반환한다.")
    void selectByMemberIdAndProductId() {
        // given
        Long memberId = Dooly_CartItem2.MEMBER.getId();
        Long productId = Dooly_CartItem2.PRODUCT.getId();

        // when
        Optional<CartItem> cartItem = cartItemDao.selectByMemberIdAndProductId(memberId, productId);

        // then
        assertThat(cartItem.get()).usingRecursiveComparison().isEqualTo(Dooly_CartItem2.ENTITY);
    }

    @Test
    @DisplayName("멤버 ID로 모든 장바구니 상품을 조회한다.")
    void selectAllByMemberId() {
        // given
        Long memberId = Dooly_CartItem1.MEMBER.getId();

        // when
        List<CartItem> cartItems = cartItemDao.selectAllByMemberId(memberId);

        // then
        assertThat(cartItems)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsAnyOf(Dooly_CartItem1.ENTITY, Dooly_CartItem2.ENTITY);
    }

    @Test
    @DisplayName("장바구니 상품 ID로 조회 시 행이 없으면 TRUE를 반환한다.")
    void isNotExistById_true() {
        // given
        Long notExistId = -1L;

        // when, then
        assertThat(cartItemDao.isNotExistById(notExistId)).isTrue();
    }

    @Test
    @DisplayName("장바구니 상품 ID로 조회 시 행이 없으면 FALSE를 반환한다.")
    void isNotExistById_false() {
        // given
        Long notExistId = Dooly_CartItem1.ID;

        // when, then
        assertThat(cartItemDao.isNotExistById(notExistId)).isFalse();
    }

    @Test
    @DisplayName("멤버 ID에 해당하는 행을 내림차순으로 조회한다.")
    void findDescByMemberId() {
        // given
        Long memberId = Ber.ID;

        // when
        List<CartItem> cartItems = cartItemDao.findDescByMemberId(memberId);

        // then
        assertThat(cartItems).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(Ber_CartItem2.ENTITY, Ber_CartItem1.ENTITY);
    }
}
