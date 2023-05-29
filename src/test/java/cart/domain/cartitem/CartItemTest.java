package cart.domain.cartitem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CartItemTest {

    @Nested
    @DisplayName("장바구니 상품 생성 시 ")
    class CreateCartItem {

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, 0})
        @DisplayName("개수가 1개보다 작으면 예외를 던진다.")
        void quantityShortage(int quantity) {
            Product product = new Product("치킨", 10000, "http://chicken.com");
            Member member = new Member("a@a.com", "password1", 10);

            assertThatThrownBy(() -> new CartItem(1L, quantity, member, product))
                    .isInstanceOf(CartItemException.class)
                    .hasMessage("장바구니 상품 수량은 최소 1개부터 가능합니다. 현재 개수: " + quantity);
        }
    }

    @Nested
    @DisplayName("checkOwner 메서드는 ")
    class CheckOwner {

        @Test
        @DisplayName("장바구니 상품의 멤버와 일치하지 않으면 예외를 던진다.")
        void notMatchMember() {
            Member member = new Member(1L, "a@a.com", "password1", 10);
            CartItem cartItem = new CartItem(member, new Product("치킨", 10000, "http://chicken.com"));
            Member newMember = new Member(2L, "b@b.com", "password2", 10);

            assertThatThrownBy(() -> cartItem.checkOwner(newMember))
                    .isInstanceOf(CartItemException.class)
                    .hasMessage("장바구니 상품을 관리할 수 있는 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("장바구니 상품의 멤버와 일치하면 예외가 발생하지 않는다.")
        void matchMember() {
            Member member = new Member(1L, "a@a.com", "password1", 10);
            CartItem cartItem = new CartItem(member, new Product("치킨", 10000, "http://chicken.com"));

            assertThatNoException().isThrownBy(() -> cartItem.checkOwner(member));
        }
    }

    @Nested
    @DisplayName("changeQuantity 메서드는 ")
    class ChangeQuantity {

        @Test
        @DisplayName("유효한 수량이 아니라면 예외를 던진다.")
        void invalidQuantity() {
            Member member = new Member(1L, "a@a.com", "password1", 10);
            CartItem cartItem = new CartItem(member, new Product("치킨", 10000, "http://chicken.com"));

            assertThatThrownBy(() -> cartItem.changeQuantity(0))
                    .isInstanceOf(CartItemException.class)
                    .hasMessage("장바구니 상품 수량은 최소 1개부터 가능합니다. 현재 개수: " + 0);
        }

        @Test
        @DisplayName("유효한 수량이라면 수량을 수정한 장바구니 상품을 반환한다.")
        void validQuantity() {
            Member member = new Member(1L, "a@a.com", "password1", 10);
            CartItem cartItem = new CartItem(member, new Product("치킨", 10000, "http://chicken.com"));

            CartItem updateCartItem = cartItem.changeQuantity(100);

            assertThat(updateCartItem.getQuantity()).isEqualTo(100);
        }
    }
}
