package cart.domain;

import static cart.fixture.TestFixture.CART_ITEMS_MEMBER_A;
import static cart.fixture.TestFixture.CART_ITEM_치킨_MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_FIXED_2000;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_PERCENTAGE_50;
import static cart.fixture.TestFixture.ORDERED_치킨;
import static cart.fixture.TestFixture.피자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import cart.exception.NotContainedItemException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CartTest {

    @Test
    void 항목을_구매한다() {
        var cart = new Cart(MEMBER_A, CART_ITEMS_MEMBER_A);

        var orderItems = cart.order(List.of(CART_ITEM_치킨_MEMBER_A()));

        assertThat(orderItems.getOrderItems())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(ORDERED_치킨);
    }

    @Test
    void 없는_항목은_주문할_수_없다() {
        var cart = new Cart(MEMBER_A, CART_ITEMS_MEMBER_A);

        assertThatThrownBy(() -> cart.order(List.of(new CartItem(MEMBER_A, 피자))))
                .isInstanceOf(NotContainedItemException.class);
    }

    @Test
    void 구매한_항목은_제거된다() {
        var cart = new Cart(MEMBER_A, CART_ITEMS_MEMBER_A);

        cart.order(List.of(CART_ITEM_치킨_MEMBER_A()));

        assertThat(cart.getItems()).doesNotContain(CART_ITEM_치킨_MEMBER_A());
    }
}
