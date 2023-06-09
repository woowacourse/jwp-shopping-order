package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static cart.exception.ErrorCode.NOT_AUTHORIZATION_MEMBER;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CartItemTest {

    private Member member;
    private Coupon coupon;
    private Product product;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "name", "password");
        coupon = new Coupon("coupon", 10, 365, LocalDateTime.MAX);

        product = new Product("치킨", 1000, "http://example.com/chicken.jpg");
    }

    @DisplayName("CartItem이 사용자의 소유인지 확인한다.")
    @Test
    void checkOwner() {
        // given
        CartItem cartItem = new CartItem(member, product);
        Member differentMember = new Member(2L, "a@a.com", "1234");

        // when & then
        assertAll(
                () -> assertDoesNotThrow(() -> cartItem.checkOwner(member)),
                () -> assertThatThrownBy(() -> cartItem.checkOwner(differentMember))
                        .isInstanceOf(AuthorizationException.class)
                        .extracting("errorCode")
                        .isEqualTo(NOT_AUTHORIZATION_MEMBER)
        );
    }
}
