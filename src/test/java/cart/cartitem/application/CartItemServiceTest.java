package cart.cartitem.application;

import cart.cartitem.domain.CartItem;
import cart.cartitem.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.dto.CartItemRequest;
import cart.cartitem.dto.CartItemResponse;
import cart.cartitem.repository.CartItemRepository;
import cart.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.member.domain.MemberTest.*;
import static cart.product.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    
    @InjectMocks
    private CartItemService cartItemService;
    
    @Test
    void 회원_정보로_장바구니_목록을_조회한다() {
        // given
        final CartItem cartItem = new CartItem(MEMBER, PRODUCT_FIRST);
        given(cartItemRepository.findByMemberId(1L)).willReturn(List.of(cartItem));
        
        // when
        final List<CartItemResponse> cartItemResponses = cartItemService.findByMember(MEMBER);
        
        // then
        assertAll(
                () -> assertThat(cartItemResponses.get(0).getQuantity()).isOne(),
                () -> assertThat(cartItemResponses.get(0).getProduct().getId()).isOne()
        );
    }
    
    @Test
    void 장바구니_담기() {
        // given
        final CartItemRequest cartItemRequest = new CartItemRequest(1L);
        given(productRepository.getProductById(1L)).willReturn(PRODUCT_FIRST);
        given(cartItemRepository.save(new CartItem(MEMBER, PRODUCT_FIRST))).willReturn(1L);
        
        // when
        final Long cartItemId = cartItemService.add(MEMBER, cartItemRequest);
        
        // then
        assertThat(cartItemId).isOne();
    }
    
    @Test
    void 장바구니_수량을_변경한다() {
        // given
        final CartItem cartItem = new CartItem(MEMBER, PRODUCT_FIRST);
        given(cartItemRepository.findById(1L)).willReturn(cartItem);
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> cartItemService.updateQuantity(MEMBER, 1L, new CartItemQuantityUpdateRequest(4L)));
    }
    
    @Test
    void 장바구니를_삭제한다() {
        // given
        final CartItem cartItem = new CartItem(MEMBER, PRODUCT_FIRST);
        given(cartItemRepository.findById(1L)).willReturn(cartItem);
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> cartItemService.remove(MEMBER, 1L));
    }
}
