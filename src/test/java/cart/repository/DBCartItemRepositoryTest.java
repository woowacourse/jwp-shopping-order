package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.MemberFixtures.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class DBCartItemRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        cartItemRepository = new DBCartItemRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("회원의 장바구니 목록을 가져온다.")
    void findByMemberIdTest() {
        // given
        Member member = MEMBER1;
        List<CartItem> expectCartItems = List.of(CART_ITEM1, CART_ITEM2);

        // when
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());

        // then
        assertThat(cartItems).isEqualTo(expectCartItems);
    }

    @Test
    @DisplayName("장바구니 상품을 저장한다.")
    void saveTest() {
        // given
        CartItem cartItem = NEW_CART_ITEM_TO_INSERT;
        Member member = cartItem.getMember();
        CartItem cartItemAfterSave = NEW_CART_ITEM;

        // when
        cartItemRepository.save(cartItem);

        // then
        assertThat(cartItemRepository.findByMemberId(member.getId())).contains(cartItemAfterSave);
    }

    @Test
    @DisplayName("장바구니 ID에 해당하는 장바구니를 가져온다.")
    void findByIdTest() {
        // given
        CartItem cartItem1 = CART_ITEM1;
        Long cartItem1Id = cartItem1.getId();

        // when
        CartItem findCartItem = cartItemRepository.findById(cartItem1Id);

        // then
        assertThat(findCartItem).isEqualTo(cartItem1);
    }

    @Test
    @DisplayName("장바구니 ID에 해당하는 장바구니를 삭제한다.")
    void deleteByIdTest() {
        // given
        CartItem cartItemToDelete = CART_ITEM1;
        Long memberId = cartItemToDelete.getMember().getId();
        List<CartItem> member1CartItems = cartItemRepository.findByMemberId(memberId);
        int expectCartItemCountAfterDelete = member1CartItems.size() - 1;

        // when
        cartItemRepository.deleteById(cartItemToDelete.getId());

        // then
        assertThat(cartItemRepository.findByMemberId(memberId)).hasSize(expectCartItemCountAfterDelete);
    }

    @Test
    @DisplayName("장바구니 항목의 개수를 수정한다.")
    void updateQuantityTest() {
        // given
        CartItem updatedCartItem1 = UPDATE_CART_ITEM1;

        // when
        cartItemRepository.updateQuantity(updatedCartItem1);

        // then
        assertThat(cartItemRepository.findById(updatedCartItem1.getId())).isEqualTo(updatedCartItem1);
    }
}