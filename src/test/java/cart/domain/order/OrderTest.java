package cart.domain.order;

import cart.TestFixture;
import cart.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    private Member member;
    private OrderProducts orderProducts;

    @BeforeEach
    void setUp() {
        member = TestFixture.getMember1();
        orderProducts = new OrderProducts(
                List.of(
                        TestFixture.getCartItem1(),
                        TestFixture.getCartItem2()
                )
        );
    }

    @DisplayName("적립금을 사용할 경우 총 가격에서 적립금을 뺀 가격만 결제한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 100, 1_000})
    void getTotalAmountTest(final int usingPoint) {
        final Order order = new Order(null, member, usingPoint, orderProducts);

        assertThat(order.getTotalAmount()).isEqualTo(orderProducts.getTotalAmount() - usingPoint);
    }
}
