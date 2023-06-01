package cart.domain;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.MEMBER_B;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.OrderException;
import cart.exception.PointException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTest {

    List<OrderItem> orderItems = List.of(new OrderItem(CHICKEN, 1));

    @Nested
    @DisplayName("DEFAULT 전략 테스트")
    class DefaultPolicyOrderTest {

        PointDiscountPolicy discountPolicy = PointDiscountPolicy.DEFAULT;
        PointEarnPolicy earnPolicy = PointEarnPolicy.DEFAULT;
        Money usedPoints = new Money(1_000);

        @Test
        @DisplayName("of 메소드는 OrderItems, usedPoints, PointDiscountPolicy, PointEarnPolicy를 전달하면 주문 관련 정보를 반환한다.")
        void ofSuccessTest() {
            Order actual = Order.of(orderItems, MEMBER_A.getId(), usedPoints, discountPolicy, earnPolicy);
            Money expectedEarnedPoints = new Money(1_000);

            assertAll(
                    () -> assertThat(actual.getOrderItems()).hasSize(1),
                    () -> assertThat(actual.getUsedPoints()).isEqualTo(usedPoints),
                    () -> assertThat(actual.getEarnedPoints()).isEqualTo(expectedEarnedPoints)
            );
        }

        @Test
        @DisplayName("of 메소드는 정책에 맞지 않는 usedPoints를 전달하면 예외가 발생한다.")
        void ofFailTestWithInvalidUsedPointsByPointDiscountPolicy() {
            Money usedPoints = new Money(1_001);

            assertThatThrownBy(() -> Order.of(orderItems, MEMBER_A.getId(), usedPoints, discountPolicy, earnPolicy))
                    .isInstanceOf(PointException.InvalidPolicy.class)
                    .hasMessageContaining("사용할 수 있는 포인트 이상을 지정했습니다.");
        }

        @Nested
        @DisplayName("Order 조회 이후 메소드 테스트")
        class AfterSelectOrderTest {

            Order order;

            @BeforeEach
            void setUp() {
                PointDiscountPolicy discountPolicy = PointDiscountPolicy.DEFAULT;
                PointEarnPolicy earnPolicy = PointEarnPolicy.DEFAULT;

                order = Order.of(orderItems, MEMBER_A.getId(), usedPoints, discountPolicy, earnPolicy);
            }

            @Test
            @DisplayName("checkOwner는 주문한 회원을 전달하면 정상적으로 동작한다.")
            void checkOwnerSuccessTest() {
                assertDoesNotThrow(() -> order.checkOwner(MEMBER_A));
            }

            @Test
            @DisplayName("checkOwner는 주문하지 않은 회원을 전달하면 예외가 발생한다.")
            void checkOwnerFailTest() {
                assertThatThrownBy(() -> order.checkOwner(MEMBER_B))
                        .isInstanceOf(OrderException.IllegalMember.class)
                        .hasMessage("해당 사용자의 주문 내역이 아닙니다.");
            }

            @Test
            @DisplayName("calculateTotalPrice는 호출하면 주문 당시의 총 상품 금액을 계산해 반환한다.")
            void calculateTotalPriceTest() {
                Money actual = order.calculateTotalPrice();
                Money expectedTotalPrice = new Money(10_000);

                assertThat(actual).isEqualTo(expectedTotalPrice);
            }

            @Test
            @DisplayName("calculatePayPrice는 호출하면 주문 당시의 실 사용 금액을 계산해 반환한다.")
            void calculatePayPriceTest() {
                Money actual = order.calculatePayPrice();
                Money expectedPayPrice = new Money(9_000);

                assertThat(actual).isEqualTo(expectedPayPrice);
            }
        }
    }
}
