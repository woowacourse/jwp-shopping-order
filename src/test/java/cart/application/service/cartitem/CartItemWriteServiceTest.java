package cart.application.service.cartitem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.ProductRepository;
import cart.application.service.cartitem.dto.CartItemCreateDto;
import cart.application.service.cartitem.dto.CartItemUpdateDto;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.ui.MemberAuth;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemWriteServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartItemWriteService cartItemWriteService;

    @Test
    @DisplayName("장바구니에 상품을 추가할 수 있다.")
    void createCartItem() {
        // givn
        Member member = new Member("test", "test@email.com", "test123");
        Product product = new Product("testProduct", 3000, "testUrl");
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.of(member));
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));
        given(cartItemRepository.createCartItem(any()))
                .willReturn(1L);

        // when
        Long createdItemId = cartItemWriteService.createCartItem(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                new CartItemCreateDto(1L));

        // then
        assertThat(createdItemId).isEqualTo(1L); // 예상 결과와 실제 결과 비교
    }

    @Test
    @DisplayName("장바구니에 상품 담기 실패 테스트 - 일치하는 사용자가 없을 때")
    void FailCreateCartWhenUnmatchedUser() {
        // givn
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> cartItemWriteService.createCartItem(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                new CartItemCreateDto(1L)))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("일치하는 사용자가 없습니다.");
    }

    @Test
    @DisplayName("장바구니에 상품 담기 실패 테스트 - 일치하는 상품이 없을 때")
    void FailCreateCartWhenUnmatchedProduct() {
        // givn
        Member member = new Member("test", "test@email.com", "test123");
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.of(member));
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> cartItemWriteService.createCartItem(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                new CartItemCreateDto(1L)))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("일치하는 상품이 없습니다.");
    }

    @Test
    @DisplayName("장바구니에 담긴 물건의 수량을 변경할 수 있다.")
    void updateQuantity() {
        // given
        Member member = new Member("test", "test@email.com", "test123");
        Product product = new Product("testProduct", 3000, "testUrl");
        CartItem cartItem = new CartItem(product, member);
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.of(member));
        given(cartItemRepository.findById(anyLong()))
                .willReturn(Optional.of(cartItem));

        // when, then
        assertDoesNotThrow(() -> cartItemWriteService.updateQuantity(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                1L,
                new CartItemUpdateDto(2)
        ));
    }

    @Test
    @DisplayName("장바구니에 담긴 물건의 수량 변경 실패 테스트 - 일치하는 사용자가 없을 때")
    void failUpdateQuantityWhenUnmatchedUser() {
        // given
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> cartItemWriteService.updateQuantity(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                1L,
                new CartItemUpdateDto(2)
        )).isInstanceOf(NoSuchElementException.class)
                .hasMessage("일치하는 사용자가 없습니다.");
    }

    @Test
    @DisplayName("장바구니에 담긴 물건의 수량 변경 실패 테스트 - 일치하는 상품이 없을 때")
    void failUpdateQuantityWhenUnmatchedProduct() {
        // given
        Member member = new Member("test", "test@email.com", "test123");
        Product product = new Product("testProduct", 3000, "testUrl");
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.of(member));
        given(cartItemRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> cartItemWriteService.updateQuantity(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                1L,
                new CartItemUpdateDto(2)
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("일치하는 상품이 없습니다.");
    }

    @Test
    @DisplayName("장바구니에 존재하는 물건을 삭제할 수 있다.")
    void remove() {
        // given
        Member member = new Member("test", "test@email.com", "test123");
        Product product = new Product("testProduct", 3000, "testUrl");
        CartItem cartItem = new CartItem(product, member);
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.of(member));
        given(cartItemRepository.findById(anyLong()))
                .willReturn(Optional.of(cartItem));

        // when, then
        assertDoesNotThrow(() -> cartItemWriteService.remove(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                1L
        ));
    }

    @Test
    @DisplayName("장바구니에 존재하는 물건 삭제 실패 테스트 - 알치하는 사용자가 없을 떄")
    void failRemoveWhenUnmatchedUser() {
        // given
        Member member = new Member("test", "test@email.com", "test123");
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> cartItemWriteService.remove(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                1L
        )).isInstanceOf(NoSuchElementException.class)
                .hasMessage("일치하는 사용자가 없습니다.");
    }

    @Test
    @DisplayName("장바구니에 존재하는 물건 삭제 실패 테스트 - 알치하는 상품이 없을 떄")
    void failRemoveWhenUnmatchedProduct() {
        // given
        Member member = new Member("test", "test@email.com", "test123");
        given(memberRepository.findMemberById(anyLong()))
                .willReturn(Optional.of(member));
        given(cartItemRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> cartItemWriteService.remove(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                1L
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("일치하는 상품이 없습니다.");
    }

}
