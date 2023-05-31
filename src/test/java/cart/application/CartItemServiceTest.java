package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import cart.exception.NonExistCartItemException;
import cart.exception.NonExistProductException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        given(productRepository.findById(1L))
                .willReturn(Optional.of(new Product(
                        1L, "밀리", BigDecimal.valueOf(10000), "http://millie.com"
                )));
        given(cartItemRepository.save(any()))
                .willReturn(
                        new CartItem(
                                1L,
                                1,
                                new Product(1L, "밀리", BigDecimal.valueOf(10000), "http://millie.com"),
                                new Member(1L, "email@email.com", "password")
                        )
                );

        // when
        Long id = cartItemService.addCart(
                new Member(1L, "email@email.com", "password"),
                new CartItemRequest(1L));

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 없는_상품을_장바구니에_추가하면_예외가_발생한다() {
        // given
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> cartItemService.addCart(
                new Member(1L, "email@email.com", "password"),
                new CartItemRequest(1L))
        ).isInstanceOf(NonExistProductException.class);

        verify(cartItemRepository, never()).save(any());
    }

    @Test
    void 사용자의_장바구니를_조회한다() {
        // given
        Member member = new Member(1L, "email@email.com", "password");
        given(cartItemRepository.findByMemberId(1L))
                .willReturn(List.of(
                        new CartItem(
                                1L,
                                1,
                                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                                member
                        ),
                        new CartItem(
                                2L,
                                10,
                                new Product(2L, "박스터", BigDecimal.valueOf(1000), "http://boxster.com"),
                                member
                        )
                ));

        // when
        List<CartItemResponse> responses = cartItemService.findByMember(member);

        // then
        assertThat(responses).map(CartItemResponse::getId)
                .containsExactly(1L, 2L);
    }

    @Test
    void 장바구니에_담긴_상품의_수량을_변경한다() {
        // given
        Member member = new Member(1L, "email@email.com", "password");
        CartItem cartItem = new CartItem(
                1L,
                1,
                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                member
        );
        given(cartItemRepository.findById(1L))
                .willReturn(Optional.of(cartItem));

        // when
        cartItemService.updateQuantity(member, 1L, new CartItemQuantityUpdateRequest(10));

        // then
        verify(cartItemRepository, never()).deleteById(1L);
        verify(cartItemRepository).updateQuantity(any());

        assertThat(cartItem.getQuantity()).isEqualTo(10);
    }

    @Test
    void 담겨있지_않은_상품의_수량을_변경할_경우_예외가_발생한다() {
        // given
        Member member = new Member(1L, "email@email.com", "password");
        given(cartItemRepository.findById(1L))
                .willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> cartItemService.updateQuantity(member, 1L, new CartItemQuantityUpdateRequest(10)))
                .isInstanceOf(NonExistCartItemException.class);
    }

    @Test
    void 변경할_수량이_0인_경우_장바구니에서_삭제한다() {
        // given
        Member member = new Member(1L, "email@email.com", "password");
        CartItem cartItem = new CartItem(
                1L,
                1,
                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                member
        );
        given(cartItemRepository.findById(1L))
                .willReturn(Optional.of(cartItem));

        // when
        cartItemService.updateQuantity(member, 1L, new CartItemQuantityUpdateRequest(0));

        // then
        verify(cartItemRepository).deleteById(1L);
        verify(cartItemRepository, never()).updateQuantity(any());
    }

    @Test
    void 수정할_장바구니의_주인이_내가_아닌_경우_예외가_발생한다() {
        // given
        Member member = new Member(1L, "email@email.com", "password");
        CartItem cartItem = new CartItem(
                1L,
                1,
                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                member
        );
        given(cartItemRepository.findById(1L))
                .willReturn(Optional.of(cartItem));

        // expect
        Member otherMember = new Member(2L, "email2@email.com", "password");
        assertThatThrownBy(() -> cartItemService.updateQuantity(otherMember, 1L, new CartItemQuantityUpdateRequest(10)))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    void 장바구니_상품을_제거한다() {
        // given
        Member member = new Member(1L, "email@email.com", "password");
        CartItem cartItem = new CartItem(
                1L,
                1,
                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                member
        );
        given(cartItemRepository.findById(1L))
                .willReturn(Optional.of(cartItem));

        // when
        cartItemService.remove(member, 1L);

        // then
        verify(cartItemRepository).deleteById(1L);
    }

    @Test
    void 담겨있지_않은_상품을_제거할_경우_예외가_발생한다() {
        // given
        Member member = new Member(1L, "email@email.com", "password");
        given(cartItemRepository.findById(1L))
                .willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> cartItemService.remove(member, 1L))
                .isInstanceOf(NonExistCartItemException.class);

        verify(cartItemRepository, never()).deleteById(1L);
    }

    @Test
    void 제거할_장바구니의_주인이_내가_아닌_경우_예외가_발생한다() {
        // given
        Member member = new Member(1L, "email@email.com", "password");
        CartItem cartItem = new CartItem(
                1L,
                1,
                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                member
        );
        given(cartItemRepository.findById(1L))
                .willReturn(Optional.of(cartItem));

        // expect
        Member otherMember = new Member(2L, "email2@email.com", "password");
        assertThatThrownBy(() -> cartItemService.remove(otherMember, 1L))
                .isInstanceOf(CartItemException.IllegalMember.class);

        verify(cartItemRepository, never()).deleteById(1L);
    }
}
