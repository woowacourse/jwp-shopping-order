package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.cartItem.CartItemQuantityUpdateRequest;
import cart.dto.cartItem.CartItemRequest;
import cart.dto.cartItem.CartItemResponse;
import cart.exception.CartItemException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.MemberFixtures.*;
import static cart.fixtures.ProductFixtures.PRODUCT3;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("회원의 장바구니 목록을 가져온다.")
    void findByMemberTest() {
        // given
        Member member = MEMBER1;
        List<CartItem> cartItems = List.of(CART_ITEM1, CART_ITEM2);
        List<CartItemResponse> expectResponses = List.of(CART_ITEM_RESPONSE1, CART_ITEM_RESPONSE2);
        when(cartItemRepository.findByMemberId(member.getId())).thenReturn(cartItems);

        // when
        List<CartItemResponse> response = cartItemService.findByMember(member);

        // then
        assertThat(response).isEqualTo(expectResponses);
    }

    @Test
    @DisplayName("장바구니를 추가한다.")
    void addTest() {
        // given
        Member member = MEMBER1_NULL_PASSWORD;
        CartItemRequest newCartItemRequest = NEW_CART_ITEM_REQUEST;
        CartItem newCartItemToAdd = NEW_CART_ITEM_TO_INSERT;
        CartItem newCartItem = NEW_CART_ITEM;
        when(productRepository.getProductById(newCartItemRequest.getProductId())).thenReturn(PRODUCT3);
        when(cartItemRepository.save(newCartItemToAdd)).thenReturn(newCartItem.getId());

        // when
        Long newCartItemId = cartItemService.add(member, newCartItemRequest);

        // then
        assertThat(newCartItemId).isEqualTo(newCartItem.getId());
    }

    @Nested
    @DisplayName("장바구니 상품의 개수를 수정한다.")
    class updateQuantityTest {

        @Test
        @DisplayName("회원의 장바구니가 아니면 예외가 발생한다.")
        void checkOwnerTest() {
            // given
            Member wrongMember = MEMBER2;
            CartItem cartItem = CART_ITEM1;
            Long cartItemId = cartItem.getId();

            CartItemQuantityUpdateRequest request = CART_ITEM_QUANTITY_UPDATE_REQUEST;
            when(cartItemRepository.findById(cartItemId)).thenReturn(cartItem);

            // when, then
            assertThatThrownBy(() -> cartItemService.updateQuantity(wrongMember, cartItemId, request))
                    .isInstanceOf(CartItemException.IllegalMember.class);
        }

        @Test
        @DisplayName("회원의 장바구니 상품이면서 요청된 개수가 0개 이면 상품을 삭제한다.")
        void deleteWhenZeroTest() {
            // given
            CartItemQuantityUpdateRequest requestToZero = CART_ITEM_QUANTITY_UPDATE_REQUEST_TO_ZERO;
            Member member = MEMBER1;
            CartItem cartItem = CART_ITEM1;
            Long cartItemId = cartItem.getId();
            when(cartItemRepository.findById(cartItemId)).thenReturn(cartItem);
            doNothing().when(cartItemRepository).deleteById(cartItemId);

            // when, then
            assertThatNoException().isThrownBy(() -> cartItemService.updateQuantity(member, cartItemId, requestToZero));
        }

        @Test
        @DisplayName("회원의 장바구니 상품이면서 수정될 상품 개수가 0보다 크면 상품의 개수가 수정된다.")
        void updateQuantityTest_success() {
            // given
            CartItemQuantityUpdateRequest request = CART_ITEM_QUANTITY_UPDATE_REQUEST;
            Member member = MEMBER1;
            CartItem cartItem = CART_ITEM1;
            Long cartItemId = cartItem.getId();
            when(cartItemRepository.findById(cartItemId)).thenReturn(cartItem);
            doNothing().when(cartItemRepository).updateQuantity(UPDATE_CART_ITEM1);

            // when, then
            assertThatNoException().isThrownBy(() -> cartItemService.updateQuantity(member, cartItemId, request));
        }
    }

    @Nested
    @DisplayName("장바구니에서 상품을 삭제한다.")
    class removeTest {

        @Test
        @DisplayName("회원의 장바구니가 아니면 예외가 발생한다.")
        void checkOwnerTest() {
            Member wrongMember = MEMBER2;
            CartItem cartItem = CART_ITEM1;
            Long cartItemId = cartItem.getId();

            when(cartItemRepository.findById(cartItemId)).thenReturn(cartItem);

            // when, then
            assertThatThrownBy(() -> cartItemService.remove(wrongMember, cartItemId))
                    .isInstanceOf(CartItemException.IllegalMember.class);
        }

        @Test
        @DisplayName("회원의 장바구니 상품이면 장바구니에서 상품을 삭제한다.")
        void removeTest_success() {
            Member member = MEMBER1;
            CartItem cartItem = CART_ITEM1;
            Long cartItemId = cartItem.getId();
            when(cartItemRepository.findById(cartItemId)).thenReturn(cartItem);
            doNothing().when(cartItemRepository).deleteById(cartItemId);

            // when, then
            assertThatNoException().isThrownBy(() -> cartItemService.remove(member, cartItemId));
        }
    }
}