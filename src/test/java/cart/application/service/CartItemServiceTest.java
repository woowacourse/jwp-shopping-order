package cart.application.service;

import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Product;
import cart.application.exception.IllegalMemberException;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.ProductRepository;
import cart.application.service.CartItemService;
import cart.presentation.dto.request.AuthInfo;
import cart.presentation.dto.request.CartItemQuantityRequest;
import cart.presentation.dto.request.CartItemRequest;
import cart.presentation.dto.response.CartItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MemberRepository memberRepository;

    private AuthInfo authInfo;
    private Member member;
    private Product pizza;

    @BeforeEach
    void setup() {
        authInfo = new AuthInfo("teo", "1234");
        member = new Member(1L, "teo", "1234", 0);
        pizza = new Product(1L, "피자", 20000, "https://a.com", 0, false);
    }

    @Test
    @DisplayName("멤버정보를 통해 장바구니 품목을 찾을 수 있다")
    void findAllCartItems() {
        // given
        when(cartItemRepository.findByMemberId(member.getId())).thenReturn(List.of(
                new CartItem(1L, 1, pizza, member)));
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        // when
        List<CartItemResponse> carItemResponses = cartItemService.getAllCartItems(authInfo);
        // then
        assertThat(carItemResponses.get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("멤버정보를 통해 장바구니에 품목을 넣을 수 있다")
    void createCartItem() {
        // given
        CartItemRequest request = new CartItemRequest(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(pizza));
        when(cartItemRepository.insert(any())).thenReturn(new CartItem(1L, 1, pizza, member));
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        // when
        Long cartItemId = cartItemService.createCartItem(authInfo, request);
        // then
        assertThat(cartItemId).isEqualTo(1L);
    }

    @Test
    @DisplayName("장바구니 품목 수량을 업데이트 할 때, 소유주가 아니라면 예외를 던진다")
    void updateQuantity_exception() {
        // given
        CartItemQuantityRequest request = new CartItemQuantityRequest(2L);
        Member another = new Member(2L, "another", "1234", 0);
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(cartItemRepository.findById(any())).thenReturn(Optional.of(
                new CartItem(1L, 1, pizza, another)));
        // when
        assertThatThrownBy(() -> cartItemService.updateQuantity(authInfo, 1L, request))
                .isInstanceOf(IllegalMemberException.class);
    }

    @Test
    @DisplayName("장바구니 품목 수량을 업데이트 할 때, 소유주라면 정상 수행된다")
    void updateQuantity() {
        // given
        CartItemQuantityRequest request = new CartItemQuantityRequest(2L);
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(cartItemRepository.findById(any())).thenReturn(Optional.of(
                new CartItem(1L, 1, pizza, member)));
        // when
        assertThatCode(() -> cartItemService.updateQuantity(authInfo, 1L, request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("장바구니 품목을 삭제할 때, 소유주가 아니라면 예외를 던진다")
    void deleteQuantity_exception() {
        // given
        Member another = new Member(2L, "another", "1234", 0);
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(cartItemRepository.findById(any())).thenReturn(Optional.of(
                new CartItem(1L, 1, pizza, another)));
        // when
        assertThatThrownBy(() -> cartItemService.remove(authInfo, 1L))
                .isInstanceOf(IllegalMemberException.class);
    }

    @Test
    @DisplayName("장바구니 품목을 삭제할 때, 소유주라면 정상 수행된다")
    void deleteQuantity() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(cartItemRepository.findById(any())).thenReturn(Optional.of(
                new CartItem(1L, 1, pizza, member)));
        // when
        assertThatCode(() -> cartItemService.remove(authInfo, 1L))
                .doesNotThrowAnyException();
    }
}
