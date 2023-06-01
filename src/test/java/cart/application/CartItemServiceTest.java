package cart.application;

import cart.Fixture;
import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.OrderCheckout;
import cart.dto.CheckoutResponse;
import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartItemDao cartItemDao;

    @DisplayName("선택된 장바구니 상품으로 주문 확인서를 생성한다")
    @Test
    void makeCheckout() {
        // given
        given(cartItemDao.findByMemberId(anyLong())).willReturn(List.of(Fixture.cartItem1));

        // when
        final CheckoutResponse actual = cartItemService.makeCheckout(Fixture.memberA, List.of(Fixture.cartItem1.getId()));

        // then
        final CheckoutResponse expected = CheckoutResponse.of(OrderCheckout.generate(Fixture.memberA.getPoints(), List.of(Fixture.cartItem1)));
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("이용자의 장바구니에 없는 상품을 주문할 경우 예외가 발생한다")
    @Test
    void makeCheckoutFail() {
        // given
        given(cartItemDao.findByMemberId(anyLong())).willReturn(List.of(Fixture.cartItem1));

        // when & then
        assertThatThrownBy(() -> cartItemService.makeCheckout(Fixture.memberA, List.of(2L)))
                .isInstanceOf(CartItemException.class);
    }
}
