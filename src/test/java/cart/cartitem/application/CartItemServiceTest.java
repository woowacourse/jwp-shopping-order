package cart.cartitem.application;

import static cart.cartitem.exception.CartItemExceptionType.NO_AUTHORITY_UPDATE_ITEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import cart.cartitem.application.dto.AddCartItemCommand;
import cart.cartitem.application.dto.UpdateCartItemQuantityCommand;
import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.cartitem.exception.CartItemException;
import cart.cartitem.presentation.dto.CartItemResponse;
import cart.common.execption.BaseExceptionType;
import cart.member.domain.Member;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import cart.product.presentation.dto.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CartItemService 은(는)")
class CartItemServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        given(productRepository.findById(1L))
                .willReturn(new Product(1L, "prod", 1000, "image"));
        given(cartItemRepository.save(any()))
                .willReturn(10L);
        AddCartItemCommand command = new AddCartItemCommand(
                new Member(1L, "mallang", "1234"),
                1L
        );

        // when
        Long add = cartItemService.add(command);

        // then
        assertThat(add).isEqualTo(10L);
    }

    @Test
    void 장바구니에_등록된_상품_수량을_변경한다() {
        // given
        CartItem cartItem = new CartItem(
                new Product(1L, "prod", 1000, "image"),
                new Member(1L, "mallang", "1234")
        );
        given(cartItemRepository.findById(1L))
                .willReturn(cartItem);
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(
                1L,
                new Member(1L, "mallang", "1234"),
                10
        );

        // when
        cartItemService.updateQuantity(command);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(10L);
    }

    @Test
    void 수량을_0개로_변경하면_제거된다() {
        // given
        CartItem cartItem = new CartItem(
                new Product(1L, "prod", 1000, "image"),
                new Member(1L, "mallang", "1234")
        );
        given(cartItemRepository.findById(1L))
                .willReturn(cartItem);
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(
                1L,
                new Member(1L, "mallang", "1234"),
                0
        );

        // when
        cartItemService.updateQuantity(command);

        // then
        then(cartItemRepository).should(times(1))
                .deleteById(1L);
    }

    @Test
    void 자신의_장바구니_상품이_아니면_변경할_수_없다() {
        // given
        CartItem cartItem = new CartItem(
                new Product(1L, "prod", 1000, "image"),
                new Member(1L, "mallang", "1234")
        );
        given(cartItemRepository.findById(1L))
                .willReturn(cartItem);
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(
                1L,
                new Member(2L, "mallang", "1234"),
                0
        );

        // when & then
        BaseExceptionType baseExceptionType = assertThrows(CartItemException.class, () ->
                cartItemService.updateQuantity(command)
        ).exceptionType();
        assertThat(baseExceptionType).isEqualTo(NO_AUTHORITY_UPDATE_ITEM);
    }

    @Test
    void 장바구니에서_상품을_제거한다() {
        // given
        Member member = new Member(1L, "mallang", "1234");
        CartItem cartItem = new CartItem(
                new Product(1L, "prod", 1000, "image"),
                member
        );
        given(cartItemRepository.findById(1L))
                .willReturn(cartItem);

        // when
        cartItemService.remove(member, 1L);

        // then
        then(cartItemRepository).should(times(1))
                .deleteById(1L);
    }

    @Test
    void 자신의_상품이_아니면_제거할_수_없다() {
        // given
        Member member = new Member(1L, "mallang", "1234");
        CartItem cartItem = new CartItem(
                new Product(1L, "prod", 1000, "image"),
                member
        );
        given(cartItemRepository.findById(1L))
                .willReturn(cartItem);

        // when & then
        BaseExceptionType baseExceptionType = assertThrows(CartItemException.class, () ->
                cartItemService.remove(new Member(2L, "a", "1234"), 1L)
        ).exceptionType();
        assertThat(baseExceptionType).isEqualTo(NO_AUTHORITY_UPDATE_ITEM);
    }

    @Test
    void 회원의_장바구니_상품들을_조회한다() {
        // given
        Member member = new Member(1L, "mallang", "image");
        List<CartItem> returned = List.of(
                new CartItem(1L, 10, new Product("p1", 100, "image"), member),
                new CartItem(2L, 10, new Product("p2", 100, "image2"), member)
        );
        given(cartItemRepository.findByMemberId(1L))
                .willReturn(returned);

        // when
        List<CartItemResponse> byMember = cartItemService.findByMember(member);

        // then
        List<CartItemResponse> expected = List.of(
                new CartItemResponse(1L, 10, new ProductResponse(null, "p1", 100, "image")),
                new CartItemResponse(2L, 10, new ProductResponse(null, "p2", 100, "image2"))
        );
        assertThat(byMember).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
