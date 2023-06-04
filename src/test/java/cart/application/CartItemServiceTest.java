package cart.application;

import cart.dao.cartitem.CartItemDao;
import cart.dao.product.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static cart.fixture.ProductFixture.피자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private CartItemDao cartItemDao;

    @InjectMocks
    private CartItemService cartItemService;

    @Test
    void 멤버로_장바구니를_조회했을_때_장바구니에_상품이_존재하지_않으면_빈_리스트를_반환한다() {
        // given
        Member member = new Member(1L, 하디.getEmail(), 하디.getPassword());
        when(cartItemDao.findAllCartItemsByMemberId(anyLong()))
                .thenReturn(Collections.emptyList());

        // when, then
        assertThat(cartItemService.findByMember(member))
                .isEmpty();
    }

    @Test
    void 장바구니에_상품을_추가할_때_없는_상품이라면_예외를_던진다() {
        // given
        when(productDao.findProductById(anyLong()))
                .thenReturn(Optional.empty());
        Member member = new Member(1L, 하디.getEmail(), 하디.getPassword());
        CartItemRequest cartItemRequest = new CartItemRequest(1L);

        // when, then
        assertThatThrownBy(() -> cartItemService.add(member, cartItemRequest))
                .isInstanceOf(CartException.class)
                .satisfies(exception -> {
                    CartException cartException = (CartException) exception;
                    assertThat(cartException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
                });
    }

    @Test
    void 장바구니에_상품을_추가할_때_이미_담긴_상품이라면_예외를_던진다() {
        // given
        Member member = new Member(1L, 하디.getEmail(), 하디.getPassword());
        CartItemRequest cartItemRequest = new CartItemRequest(1L);

        when(productDao.findProductById(anyLong()))
                .thenReturn(Optional.of(
                        new Product(1L, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock())
                ));
        when(cartItemDao.findCartItemByMemberIdAndProductId(anyLong(), anyLong()))
                .thenReturn(Optional.of(new CartItem(현구막, 피자)));

        // when, then
        assertThatThrownBy(() -> cartItemService.add(member, cartItemRequest))
                .isInstanceOf(CartException.class)
                .satisfies(exception -> {
                    CartException cartException = (CartException) exception;
                    assertThat(cartException.getErrorCode()).isEqualTo(ErrorCode.CART_ITEM_DUPLICATED);
                });
    }

    @Test
    void 장바구니에_상품의_수량을_변경할_때_해당장바구니가_없다면_예외를_던진다() {
        // given
        Member member = new Member(1L, 하디.getEmail(), 하디.getPassword());
        Long cartId = 3L;
        CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest = new CartItemQuantityUpdateRequest(2L);

        // when, then
        assertThatThrownBy(() -> cartItemService.updateQuantity(member, cartId, cartItemQuantityUpdateRequest))
                .isInstanceOf(CartException.class)
                .satisfies(exception -> {
                    CartException cartException = (CartException) exception;
                    assertThat(cartException.getErrorCode()).isEqualTo(ErrorCode.CART_ITEM_NOT_FOUND);
                });
    }

    @Test
    void 장바구니에_상품을_삭제할_때_해당장바구니가_없다면_예외를_던진다() {
        // given
        Member member = new Member(1L, 하디.getEmail(), 하디.getPassword());
        Long cartId = 3L;

        // when, then
        assertThatThrownBy(() -> cartItemService.remove(member, cartId))
                .isInstanceOf(CartException.class)
                .satisfies(exception -> {
                    CartException cartException = (CartException) exception;
                    assertThat(cartException.getErrorCode()).isEqualTo(ErrorCode.CART_ITEM_NOT_FOUND);
                });
    }
}
