package cart.service;

import cart.dao.CartItemDao;
import cart.domain.cartItem.CartItem;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.common.fixture.DomainFixture.MEMBER_HUCHU;
import static cart.common.fixture.DomainFixture.PRODUCT_CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private CartItemDao cartItemDao;

    @Test
    void 상품_id로_장바구니_아이템을_찾는다() {
        //given
        final Long productId = PRODUCT_CHICKEN.getId();
        Mockito.when(cartItemDao.findByMemberId(MEMBER_HUCHU.getId()))
                .thenReturn(List.of(new CartItem(1L, 1, PRODUCT_CHICKEN, MEMBER_HUCHU)));

        //when
        final CartItemResponse cartItemResponse = cartItemService.findByProductId(MEMBER_HUCHU, productId);

        //then
        assertThat(cartItemResponse).usingRecursiveComparison()
                .isEqualTo(CartItemResponse.of(new CartItem(1L, 1, PRODUCT_CHICKEN, MEMBER_HUCHU)));
    }

    @Test
    void 상품_id로_장바구니_아이템을_찾을_때_장바구니_아이템이_존재하지_않으면_예외를_던진다() {
        //given
        final Long wrongProductId = Long.MIN_VALUE;
        Mockito.when(cartItemDao.findByMemberId(MEMBER_HUCHU.getId()))
                .thenReturn(List.of(new CartItem(1L, 1, PRODUCT_CHICKEN, MEMBER_HUCHU)));

        //expect
        assertThatThrownBy(() -> cartItemService.findByProductId(MEMBER_HUCHU, wrongProductId))
                .isInstanceOf(CartItemException.IllegalProduct.class)
                .hasMessage("회원의 장바구니에 해당 상품이 존재하지 않습니다; productId=-9223372036854775808");
    }

}
