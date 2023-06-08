package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.respository.cartitem.CartItemRepository;
import cart.domain.respository.product.ProductRepository;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import cart.exception.CartItemException;
import cart.exception.CartItemException.CartItemNotExistException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @InjectMocks
    CartItemService cartItemService;

    @DisplayName("멤버가 담은 장바구니 상품들의 정보를 조회한다.")
    @Test
    void findByMember() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        when(cartItemRepository.findByMemberId(any())).thenReturn(List.of(new CartItem(1L, 10, product, member)));

        //when
        final List<CartItemResponse> cartItemResponses = cartItemService.findByMember(member);

        //then
        Assertions.assertAll(
            () -> assertThat(cartItemResponses).hasSize(1),
            () -> assertThat(cartItemResponses.get(0).getId()).isEqualTo(1L),
            () -> assertThat(cartItemResponses.get(0).getQuantity()).isEqualTo(10),
            () -> assertThat(cartItemResponses.get(0).getProduct()).usingRecursiveComparison().isEqualTo(product)
        );
    }

    @DisplayName("장바구니 상품을 추가한다.")
    @Test
    void add() {
        //given
        final Member member = new Member(1L, "email", "password");
        final CartItemRequest cartItemRequest = new CartItemRequest(1L);
        final Product product = new Product(1L, "product", 1000, "imageUrl");

        when(productRepository.getProductById(any())).thenReturn(Optional.of(product));

        //when
        cartItemService.add(member, cartItemRequest);

        //then
        verify(productRepository).getProductById(any());
        verify(cartItemRepository).save(any());
    }

    @DisplayName("존재하지 않는 상품을 장바구니에 추가하려 할 때")
    @Test
    void add_fail() {
        //given
        final Member member = new Member(1L, "email", "password");
        final CartItemRequest cartItemRequest = new CartItemRequest(1L);

        when(productRepository.getProductById(any())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> cartItemService.add(member, cartItemRequest))
            .isInstanceOf(CartItemNotExistException.class);
    }

    @DisplayName("장바구니 상품의 수량을 업데이트한다.")
    @Test
    void updateQuantity() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Product product = new Product(1L, "product", 1000, "imageUrl");

        final Optional<CartItem> cartItem = Optional.of(new CartItem(1L, 10, product, member));
        when(cartItemRepository.findById(any())).thenReturn(cartItem);

        //when
        cartItemService.updateQuantity(member, 1L, new CartItemQuantityUpdateRequest(3));

        //then
        verify(cartItemRepository).updateQuantity(cartItem.get());
        assertThat(cartItem.get().getQuantity()).isEqualTo(3);
    }

    @DisplayName("장바구니 상품의 수량을 업데이트시 수량이 0이면 장바구니 상품을 제거한다.")
    @Test
    void updateQuantity_zeroQuantity() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Product product = new Product(1L, "product", 1000, "imageUrl");

        final Optional<CartItem> cartItem = Optional.of(new CartItem(1L, 10, product, member));
        when(cartItemRepository.findById(any())).thenReturn(cartItem);

        //when
        cartItemService.updateQuantity(member, 1L, new CartItemQuantityUpdateRequest(0));

        //then
        verify(cartItemRepository).deleteById(cartItem.get().getId());
    }

    @DisplayName("수량을 업데이트할 장바구니 상품이 없는 경우 예외를 발생한다.")
    @Test
    void updateQuantity_notExistCartItem() {
        //given
        final Member member = new Member(1L, "email", "password");

        when(cartItemRepository.findById(any())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> cartItemService.updateQuantity(member, 1L, new CartItemQuantityUpdateRequest(3)))
            .isInstanceOf(CartItemNotExistException.class);
    }

    @DisplayName("업데이트할 장바구니 상품의 구매자가 다른 경우 예외를 발생한다.")
    @Test
    void updateQuantity_IllegalMember() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Member member2 = new Member(2L, "email2", "password");
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final Optional<CartItem> cartItem = Optional.of(new CartItem(1L, 10, product, member));

        when(cartItemRepository.findById(any())).thenReturn(cartItem);

        //when
        //then
        assertThatThrownBy(() -> cartItemService.updateQuantity(member2, 1L, new CartItemQuantityUpdateRequest(3)))
            .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @DisplayName("장바구니 상품을 삭제한다.")
    @Test
    void remove() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final Optional<CartItem> cartItem = Optional.of(new CartItem(1L, 10, product, member));

        when(cartItemRepository.findById(any())).thenReturn(cartItem);

        //when
        cartItemService.remove(member, 1L);

        //then
        verify(cartItemRepository).deleteById(1L);
    }

    @DisplayName("삭제하려는 장바구니 상품이 존재하지 않을시 예외를 발생한다.")
    @Test
    void remove_notExist() {
        //given
        final Member member = new Member(1L, "email", "password");

        when(cartItemRepository.findById(any())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> cartItemService.remove(member, 1L))
            .isInstanceOf(CartItemNotExistException.class);
    }

    @DisplayName("장바구니 상품을 담지 않은 멤버가 장바구니 상품 삭제시 예외를 발생한다.")
    @Test
    void remove_IllegalMember() {
        //given
        final Member member = new Member(1L, "email", "password");
        final Member member2 = new Member(2L, "email2", "password2");
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final Optional<CartItem> cartItem = Optional.of(new CartItem(1L, 10, product, member));

        when(cartItemRepository.findById(any())).thenReturn(cartItem);

        //when
        //then
        assertThatThrownBy(() -> cartItemService.remove(member2, 1L))
            .isInstanceOf(CartItemException.IllegalMember.class);
    }
}
