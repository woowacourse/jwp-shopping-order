package cart.domain.cart;

import cart.exception.QuantityExceedsCartException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cart.fixture.CartItemFixture.createCartItem;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CartItemTest {

    @DisplayName("상품을 가지고 있는지 확인한다.")
    @Test
    void check_has_product() {
        // given
        CartItem cartItem = createCartItem();

        // when
        boolean result = cartItem.hasProduct(1L);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("현재 보유중인 수량보다 많은 양을 요구하면 예외를 던진다.")
    @Test
    void throws_exception_when_request_quantity_is_more_than_has_item_quantities() {
        // given
        CartItem cartItem = createCartItem();

        // when & then
        assertThatThrownBy(() -> cartItem.validateQuantity(100))
                .isInstanceOf(QuantityExceedsCartException.class);
    }

    @DisplayName("할인이 적용된 가격을 반환한다. 만약 할인중이 아니라면 원래 가격을 반환한다.")
    @Test
    void returns_price_applied_discount_or_origin_price() {
        // given
        CartItem discountItem = createCartItem();
        CartItem normalItem = createCartItem();

        // when
        discountItem.getProduct().applySale(10);

        // then
        assertThat(discountItem.getAppliedDiscountOrOriginPrice(3)).isEqualTo(54000);
        assertThat(normalItem.getAppliedDiscountOrOriginPrice(3)).isEqualTo(60000);
    }

    @DisplayName("수량이 0개인지 확인한다.")
    @Test
    void check_is_empty_quantity() {
        // given
        CartItem cartItem = createCartItem();

        // when
        boolean result = cartItem.isEmptyQuantity();

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("구매하면 수량이 일정부분 만큼 감소한다.")
    @Test
    void decrease_quantity_when_buy_product() {
        // given
        CartItem cartItem = createCartItem();

        // when
        cartItem.buy(5);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(5);
    }

    @DisplayName("같은 id인지 확인한다.")
    @Test
    void check_is_same_id() {
        // given
        CartItem cartItem = createCartItem();

        // when
        boolean result = cartItem.isSame(1L);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("id가 존재한다면 true를 반환한다.")
    @Test
    void returns_true_when_id_not_null() {
        // given
        CartItem cartItem = createCartItem();

        // when
        boolean result = cartItem.isExistAlready();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("수량을 조절한다.")
    @Test
    void change_quantity() {
        // given
        CartItem cartItem = createCartItem();

        // when
        cartItem.changeQuantity(5);
        cartItem.addQuantity();

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(6);
    }

}
