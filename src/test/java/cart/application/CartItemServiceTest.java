package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.IllegalMemberException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "id", "password");
    }

    @Test
    void save() {
        //given
        final Product product = new Product(1L, "치킨", 30000, "example.com/chicken");
        final CartItemRequest request = new CartItemRequest(1L);
        given(productRepository.findById(eq(1L))).willReturn(product);
        given(cartItemRepository.save(any(CartItem.class))).willReturn(1L);

        //when
        final Long id = cartItemService.save(member, request);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void findByMember() {
        //given
        final Product product1 = new Product(1L, "치킨", 30000, "example.com/chicken");
        final Product product2 = new Product(2L, "피자", 20000, "example.com/pizza");
        final CartItem item1 = new CartItem(1L, 2, product1, member);
        final CartItem item2 = new CartItem(2L, 2, product2, member);
        final List<Product> expectedProducts = List.of(product1, product2);
        final List<CartItem> expected = List.of(item1, item2);
        given(cartItemRepository.findByMemberId(eq(member.getId()))).willReturn(expected);

        //when
        final List<CartItemResponse> responses = cartItemService.findByMemberId(member.getId());

        //then
        assertAll(
                () -> assertThat(responses).usingRecursiveComparison()
                        .ignoringFields("product")
                        .isEqualTo(expected),
                () -> assertThat(responses).extracting(CartItemResponse::getProduct)
                        .usingRecursiveComparison()
                        .isEqualTo(expectedProducts)
        );
    }

    @Nested
    @DisplayName("장바구니 아이템의 수량 변경 시 ")
    class UpdateQuantity {

        @Test
        @DisplayName("올바른 정보가 입력되면 수량 변경 로직을 수행한다.")
        void updateQuantity() {
            //given
            final CartItem cartItem = new CartItem(1L, 2, new Product(1L, "치킨", 30000, "example.com/chicken"), member);
            given(cartItemRepository.findById(eq(1L))).willReturn(cartItem);
            willDoNothing().given(cartItemRepository).update(eq(cartItem));

            //when
            //then
            assertThatNoException().isThrownBy(() -> cartItemService.updateQuantity(
                    member,
                    1L,
                    new CartItemQuantityUpdateRequest(3)
            ));
        }

        @Test
        @DisplayName("변경될 수량이 0이라면 장바구니 아이템 삭제 로직을 수행한다.")
        void updateQuantityWithQuantityZero() {
            //given
            final CartItem cartItem = new CartItem(1L, 2, new Product(1L, "치킨", 30000, "example.com/chicken"), member);
            given(cartItemRepository.findById(eq(1L))).willReturn(cartItem);
            willDoNothing().given(cartItemRepository).deleteById(eq(1L));

            //when
            //then
            assertThatNoException().isThrownBy(() -> cartItemService.updateQuantity(
                    member,
                    1L,
                    new CartItemQuantityUpdateRequest(0)
            ));
        }

        @Test
        @DisplayName("장바구니 아이템의 소유자가 인증된 사용자와 다르다면 예외를 던진다.")
        void updateQuantityWithInvalidMember() {
            //given
            final Member invalidMember = new Member(2L, "id2", "password2");
            final CartItem cartItem = new CartItem(1L, 2, new Product(1L, "치킨", 30000, "example.com/chicken"),
                    invalidMember);
            given(cartItemRepository.findById(eq(1L))).willReturn(cartItem);

            //when
            //then
            assertThatThrownBy(() -> cartItemService.updateQuantity(member, 1L, new CartItemQuantityUpdateRequest(3)))
                    .isInstanceOf(IllegalMemberException.class);
        }
    }

    @Nested
    @DisplayName("아이디로 장바구니 아이템 삭제 시 ")
    class DeleteById {
        @Test
        @DisplayName("정상적인 멤버 정보라면 삭제 로직을 수행한다.")
        void deleteById() {
            //given
            final CartItem cartItem = new CartItem(1L, 2, new Product(1L, "치킨", 30000, "example.com/chicken"), member);
            given(cartItemRepository.findById(eq(1L))).willReturn(cartItem);
            willDoNothing().given(cartItemRepository).deleteById(1L);

            //when
            //then
            assertThatNoException().isThrownBy(() -> cartItemService.deleteById(member, 1L));
        }

        @Test
        @DisplayName("장바구니 아이템의 소유자가 인증된 사용자와 다르다면 예외를 던진다.")
        void deleteByIdWithInvalidMember() {
            //given
            final Member invalidMember = new Member(2L, "id2", "password2");
            final CartItem cartItem = new CartItem(
                    1L,
                    2,
                    new Product(1L, "치킨", 30000, "example.com/chicken"),
                    invalidMember
            );
            given(cartItemRepository.findById(eq(1L))).willReturn(cartItem);

            //when
            //then
            assertThatThrownBy(() -> cartItemService.deleteById(member, 1L))
                    .isInstanceOf(IllegalMemberException.class);
        }
    }
}
