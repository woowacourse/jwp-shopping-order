package cart.application;

import static cart.fixture.CartItemFixture.CART_ITEM_1;
import static cart.fixture.CartItemFixture.CART_ITEM_2;
import static cart.fixture.CartItemFixture.CART_ITEM_3;
import static cart.fixture.CartItemRequestFixture.CART_ITEM_REQUEST_1;
import static cart.fixture.CartItemsFixture.CART_ITEMS_1;
import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.MemberFixture.MEMBER_2;
import static cart.fixture.PageInfoFixture.PAGE_INFO_1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;

import cart.dao.MemberDao;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemResponse;
import cart.dto.cartitem.CartItemsResponse;
import cart.dto.page.PageResponse;
import cart.exception.CartItemException;
import cart.repository.CartItemRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(CartItemService.class)
class CartItemServiceTest {

    @Autowired
    CartItemService cartItemService;

    @MockBean
    CartItemRepository cartItemRepository;

    @MockBean
    MemberDao memberDao;

    @Test
    @DisplayName("특정 사용자의 장바구니 상품들을 조회한다.")
    void findByMember() {
        // given
        willReturn(CART_ITEMS_1).given(cartItemRepository).findByMember(any());

        // when
        List<CartItemResponse> cartItemResponses = cartItemService.findByMember(MEMBER_1);

        // then
        assertAll(
                () -> assertThat(cartItemResponses).hasSize(3),
                () -> assertThat(cartItemResponses.get(0))
                        .usingRecursiveComparison()
                        .isEqualTo(CartItemResponse.from(CART_ITEM_1)),
                () -> assertThat(cartItemResponses.get(1))
                        .usingRecursiveComparison()
                        .isEqualTo(CartItemResponse.from(CART_ITEM_2)),
                () -> assertThat(cartItemResponses.get(2))
                        .usingRecursiveComparison()
                        .isEqualTo(CartItemResponse.from(CART_ITEM_3))
        );
    }

    @Test
    @DisplayName("장바구니에 상품을 추가한다.")
    void add() {
        // given
        willReturn(1L).given(cartItemRepository).add(any(), anyLong());

        // when
        Long cartItemId = cartItemService.add(MEMBER_1, CART_ITEM_REQUEST_1);

        // then
        assertThat(cartItemId).isEqualTo(1L);
    }

    @Test
    @DisplayName("장바구니의 특정 상품의 수량을 수정한다.")
    void updateQuantity() {
        // given
        willReturn(CART_ITEM_1).given(cartItemRepository).findById(anyLong());
        willDoNothing().given(cartItemRepository).updateQuantity(any());
        CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(10);

        // when, then
        assertDoesNotThrow(
                () -> cartItemService.updateQuantity(MEMBER_1, CART_ITEM_1.getId(), request)
        );
    }

    @Test
    @DisplayName("장바구니의 특정 상품의 수량을 수정할 때, 사용자 정보가 다르면 예외가 발생한다.")
    void updateQuantity_fail() {
        // given
        willReturn(CART_ITEM_1).given(cartItemRepository).findById(anyLong());
        willDoNothing().given(cartItemRepository).updateQuantity(any());
        CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(10);

        // when, then
        assertThatThrownBy(
                () -> cartItemService.updateQuantity(MEMBER_2, CART_ITEM_1.getId(), request)
        ).isInstanceOf(CartItemException.class)
                .hasMessage("Illegal member attempts to cart; cartItemId=1, memberId=2");
    }

    @Test
    @DisplayName("장바구니에서 상품을 삭제한다.")
    void remove() {
        // given
        willReturn(CART_ITEM_1).given(cartItemRepository).findById(anyLong());

        // when, then
        assertDoesNotThrow(
                () -> cartItemService.remove(MEMBER_1, CART_ITEM_1.getId())
        );
    }

    @Test
    @DisplayName("장바구니에서 상품을 삭제할 때, 사용자 정보가 다르면 예외가 발생한다.")
    void remove_fail() {
        // given
        willReturn(CART_ITEM_1).given(cartItemRepository).findById(anyLong());

        // when, then
        assertThatThrownBy(
                () -> cartItemService.remove(MEMBER_2, CART_ITEM_1.getId())
        ).isInstanceOf(CartItemException.class)
                .hasMessage("Illegal member attempts to cart; cartItemId=1, memberId=2");
    }

    @Test
    @DisplayName("특정 사용자의 전체 장바구니 상품을 조회한다.")
    void findCartItems() {
        // given
        willReturn(CART_ITEMS_1).given(cartItemRepository).findCartItemsByPage(any(), anyInt(), anyInt());
        willReturn(PAGE_INFO_1).given(cartItemRepository).findPageInfo(any(), anyInt(), anyInt());

        // when
        CartItemsResponse cartItems = cartItemService.findCartItems(MEMBER_1, 1, 1);

        // then
        assertAll(
                () -> assertThat(cartItems.getCartItems().get(0))
                        .usingRecursiveComparison()
                        .isEqualTo(CartItemResponse.from(CART_ITEM_1)),
                () -> assertThat(cartItems.getCartItems().get(1))
                        .usingRecursiveComparison()
                        .isEqualTo(CartItemResponse.from(CART_ITEM_2)),
                () -> assertThat(cartItems.getCartItems().get(2))
                        .usingRecursiveComparison()
                        .isEqualTo(CartItemResponse.from(CART_ITEM_3)),
                () -> assertThat(cartItems.getPagination())
                        .usingRecursiveComparison()
                        .isEqualTo(PageResponse.from(PAGE_INFO_1))
        );
    }
}
