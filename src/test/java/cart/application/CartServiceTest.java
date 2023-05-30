package cart.application;

import static cart.fixture.TestFixture.CART_ITEMS_MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cart.domain.Cart;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemService cartItemService;
    @InjectMocks
    private CartService cartService;

    @Test
    void 장바구니를_가져온다() {
        doReturn(CART_ITEMS_MEMBER_A).when(cartItemService).findByMember(eq(MEMBER_A));

        var cart = cartService.getCartOf(MEMBER_A);

        assertThat(cart.getItems()).containsExactlyInAnyOrderElementsOf(CART_ITEMS_MEMBER_A);
    }

    @Test
    void 장바구니를_저장한다() {
        var cart = new Cart(MEMBER_A, CART_ITEMS_MEMBER_A);

        cartService.save(cart);

        verify(cartItemService, times(1)).removeAllOf(eq(MEMBER_A));
        verify(cartItemService, times(1)).add(eq(CART_ITEMS_MEMBER_A));
    }
}
