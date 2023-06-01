package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.CartItem;
import cart.domain.Quantity;
import cart.exception.CartItemNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;

class CartItemRepositoryTest extends RepositoryTest {

    private long saveCartItemAndGetId() {
        return cartItemRepository.save(dummyCartItem);
    }

    @Test
    void 새로운_객체를_추가한다() {
        // given
        insertDummyMemberAndProduct();

        // when
        Long savedId = cartItemRepository.save(dummyCartItem);

        // then
        assertThat(savedId).isEqualTo(savedId);
    }

    @Test
    void ID로_객체를_조회한다() {
        // given
        insertDummyMemberAndProduct();
        long savedId = saveCartItemAndGetId();

        // when
        CartItem result = cartItemRepository.findById(savedId);

        // then
        assertThat(result).isEqualTo(dummyCartItem);
    }

    @Test
    void 회원ID로_객체를_조회한다() {
        // given
        insertDummyMemberAndProduct();
        saveCartItemAndGetId();

        // when
        List<CartItem> result = cartItemRepository.findByMemberId(dummyMember.getId());

        // then
        assertThat(result).contains(dummyCartItem);
    }

    @Test
    void 새로운_객체로_수정한다() {
        // given
        insertDummyMemberAndProduct();
        long savedId = saveCartItemAndGetId();

        CartItem newCartItem = new CartItem(savedId, dummyMember, dummyProduct, new Quantity(99));

        // when
        cartItemRepository.update(newCartItem);

        // then
        CartItem result = cartItemRepository.findById(savedId);
        assertThat(result.getQuantityValue()).isEqualTo(99);
    }

    @Test
    void ID로_저장된_객체를_삭제한다() {
        // given
        insertDummyMemberAndProduct();
        long savedId = saveCartItemAndGetId();

        // when
        cartItemRepository.deleteById(savedId);

        // then
        assertThatThrownBy(() -> cartItemRepository.findById(savedId))
                .isInstanceOf(CartItemNotFoundException.class);
    }
}
