package cart.domain.refund;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.cartitem.CartItem;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.order.BasicOrder;
import cart.domain.product.Product;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefundPolicyCompositeTest {

    private RefundPolicyComposite refundPolicyComposite;

    @BeforeEach
    void setUp() {
        refundPolicyComposite = new RefundPolicyComposite(
            List.of(new FullRefundPolicy(), new HalfRefundPolicy())
        );
    }

    @Test
    @DisplayName("주문 시간의 7일 이내라면 환불 정책을 반환한다.")
    void getRefundPolicies_success() {
        // given
        final LocalDateTime 주문_시간 = LocalDateTime.now().minusDays(6);
        final Member 져니 = Member.create(1L, "journey", EncryptedPassword.create("password"));
        final CartItem 치킨_장바구니_아이템 = new CartItem(1L, 10, new Product(1L,
            "치킨", 20000, "chicken_image_url", false));
        final CartItem 피자_장바구니_아이템 = new CartItem(2L, 5, new Product(2L,
            "피자", 30000, "pizza_image_url", false));
        final BasicOrder 주문 = new BasicOrder(져니, 3000, 주문_시간,
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);

        // expected
        assertDoesNotThrow(() -> refundPolicyComposite.getRefundPolicies(주문, LocalDateTime.now()));
    }

    @Test
    @DisplayName("주문 시간의 7일 이상이라면 예외를 반환한다.")
    void getRefundPolicies_fail() {
        // given
        final LocalDateTime 주문_시간 = LocalDateTime.now().minusDays(7);
        final Member 져니 = Member.create(1L, "journey", EncryptedPassword.create("password"));
        final CartItem 치킨_장바구니_아이템 = new CartItem(1L, 10, new Product(1L,
            "치킨", 20000, "chicken_image_url", false));
        final CartItem 피자_장바구니_아이템 = new CartItem(2L, 5, new Product(2L,
            "피자", 30000, "pizza_image_url", false));
        final BasicOrder 주문 = new BasicOrder(져니, 3000, 주문_시간,
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);

        // expected
        assertThatThrownBy(() -> refundPolicyComposite.getRefundPolicies(주문, LocalDateTime.now()))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.ORDER_CANNOT_CANCEL);
    }
}
