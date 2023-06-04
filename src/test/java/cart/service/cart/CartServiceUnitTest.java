package cart.service.cart;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.exception.MemberNotOwnerException;
import cart.repository.cart.CartRepository;
import cart.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static cart.fixture.CartFixture.createCart;
import static cart.fixture.CartFixture.createCart2;
import static cart.fixture.CartItemFixture.createCartItem;
import static cart.fixture.MemberFixture.createMember;
import static cart.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceUnitTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("모든 아이템을 찾는다.")
    @Test
    void find_all_products() {
        // given
        Member member = createMember();
        Cart cart = createCart();

        given(cartRepository.findCartByMemberId(member.getId())).willReturn(cart);

        // when
        cartService.findAllCartItems(member);

        // then
        assertThat(cart.getCartItems().size()).isEqualTo(2);
    }

    @DisplayName("아이템을 추가한다.")
    @Test
    void add_cart_item() {
        // given
        Member member = createMember();
        Cart cart = createCart();
        CartItem cartItem = createCartItem();
        Product product = createProduct();
        CartItemRequest req = new CartItemRequest(1L);

        given(cartRepository.findCartByMemberId(member.getId())).willReturn(cart);
        given(productRepository.findProductById(req.getProductId())).willReturn(product);
        given(cartRepository.isExistAlreadyCartItem(cart, product)).willReturn(true);
        given(cartRepository.findCartItem(cart, req.getProductId())).willReturn(cartItem);

        // when
        Long id = cartService.add(member, req);

        // then
        verify(cartRepository).addCartItemQuantity(any(CartItem.class));
    }

    @DisplayName("카트 아이템의 수량을 변경한다.")
    @Test
    void update_quantity() {
        // given
        Member member = createMember();
        Cart cart = createCart();
        CartItem cartItem = createCartItem();
        CartItemQuantityUpdateRequest req = new CartItemQuantityUpdateRequest(100);

        given(cartRepository.findCartItemById(any())).willReturn(cartItem);
        given(cartRepository.findCartByCartItemId(any())).willReturn(cart);

        // when
        cartService.updateQuantity(member, 1L, req);

        // then
        verify(cartRepository).updateCartItemQuantity(any(CartItem.class), eq(req.getQuantity()));
    }

    @DisplayName("멤버가 일치하지 않으면 예외를 발생시킨다.")
    @Test
    void throws_exception_when_member_not_equals() {
        // given
        Member member = createMember();
        Cart cart = createCart2();
        CartItem cartItem = createCartItem();
        CartItemQuantityUpdateRequest req = new CartItemQuantityUpdateRequest(100);

        given(cartRepository.findCartItemById(any())).willReturn(cartItem);
        given(cartRepository.findCartByCartItemId(any())).willReturn(cart);

        // when & then
        assertThatThrownBy(() -> cartService.updateQuantity(member, 1L, req))
                .isInstanceOf(MemberNotOwnerException.class);
    }

    @DisplayName("아이템을 제거한다.")
    @Test
    void remove_cart_item() {
        // given
        Member member = createMember();
        Cart cart = createCart();
        given(cartRepository.findCartByCartItemId(any())).willReturn(cart);

        // when
        cartService.remove(member, 1L);

        // then
        verify(cartRepository).deleteCartItemById(any());
    }

    @DisplayName("모든 아이템을 제거한다.")
    @Test
    void delete_all_items() {
        // given
        Member member = createMember();
        Cart cart = createCart();

        given(cartRepository.findCartByMemberId(member.getId())).willReturn(cart);

        // when
        cartService.deleteAllCartItems(member);

        // then
        verify(cartRepository).deleteAllCartItems(any(Cart.class));
    }
}
