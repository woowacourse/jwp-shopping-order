package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;

import java.util.List;

import cart.domain.CartItem;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.fixture.CartItemFixture;
import cart.fixture.MemberFixture;
import cart.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    private CartItemService cartItemService;

    @BeforeEach
    void setUp() {
        cartItemService = new CartItemService(cartItemRepository);
    }

    @Test
    void addCartItem() {
        given(cartItemRepository.addCartItem(any(), anyLong())).willReturn(CartItem.of(1L, CartItemFixture.CHICKEN));
        final CartItemResponse result = cartItemService.addCartItem(MemberFixture.MEMBER, new CartItemRequest(1L));
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.getProduct().getName()).isEqualTo("치킨"),
                () -> assertThat(result.getProduct().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.getProduct().getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getQuantity()).isEqualTo(1)
        );
    }

    @Test
    void findByMember() {
        given(cartItemRepository.findByMember(any())).willReturn(List.of(
                CartItem.of(1L, CartItemFixture.CHICKEN),
                CartItem.of(2L, CartItemFixture.PIZZA)
        ));
        final List<CartItemResponse> result = cartItemService.findByMember(MemberFixture.MEMBER);
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getProduct().getName()).isEqualTo("치킨"),
                () -> assertThat(result.get(0).getProduct().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.get(0).getProduct().getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.get(0).getQuantity()).isEqualTo(1),
                () -> assertThat(result.get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getProduct().getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getProduct().getName()).isEqualTo("피자"),
                () -> assertThat(result.get(1).getProduct().getPrice()).isEqualTo(15_000),
                () -> assertThat(result.get(1).getProduct().getImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(result.get(1).getQuantity()).isEqualTo(1)
        );
    }

    @Test
    void updateQuantity() {
        doNothing().when(cartItemRepository).updateQuantity(any(), anyLong(), anyInt());
        assertThatCode(() -> cartItemService.updateQuantity(MemberFixture.MEMBER, 1L, new CartItemQuantityUpdateRequest(10)))
                .doesNotThrowAnyException();
    }

    @Test
    void deleteCartItem() {
        doNothing().when(cartItemRepository).deleteCartItem(any(), anyLong());
        assertThatCode(() -> cartItemService.deleteCartItem(MemberFixture.MEMBER, 1L))
                .doesNotThrowAnyException();
    }
}
