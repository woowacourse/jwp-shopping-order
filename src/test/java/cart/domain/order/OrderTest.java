package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.OrderException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @Nested
    @DisplayName("주문 생성 시 ")
    class Create {

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1})
        @DisplayName("사용한 포인트가 0원보다 작으면 예외를 던진다.")
        void invalidPoint(int usedPoint) {
            assertThatThrownBy(() -> new Order(null, null, usedPoint))
                    .isInstanceOf(OrderException.class)
                    .hasMessage("포인트 적용은 0원 이상만 가능합니다.");
        }

        @Test
        @DisplayName("주문 상품 가격 합이 50000원보다 작은 경우 배송료는 3000원이다.")
        void applyDeliveryFee() {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            Product product = new Product("치킨", 10000, "http://chicken.com");
            OrderProduct orderProduct = new OrderProduct(product, 3);
            Order order = new Order(member, List.of(orderProduct), 0);

            int result = order.getDeliveryFee();

            assertThat(result).isEqualTo(3000);
        }

        @Test
        @DisplayName("주문 상품 가격 합이 50000원 이상인 경우 배송료는 0원이다.")
        void feeDeliveryFee() {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            Product product = new Product("치킨", 10000, "http://chicken.com");
            OrderProduct orderProduct = new OrderProduct(product, 5);
            Order order = new Order(member, List.of(orderProduct), 0);

            int result = order.getDeliveryFee();

            assertThat(result).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("checkOwner 메서드는 ")
    class CheckOwner {

        @Test
        @DisplayName("주문한 멤버인 경우 예외를 던지지 않는다.")
        void validMember() {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            Product product = new Product("치킨", 10000, "http://chicken.com");
            OrderProduct orderProduct = new OrderProduct(product, 5);
            Order order = new Order(member, List.of(orderProduct), 0);

            assertThatNoException().isThrownBy(() -> order.checkOwner(member));
        }

        @Test
        @DisplayName("주문한 멤버가 아닌 경우 예외를 던진다.")
        void invalidMember() {
            Member memberA = new Member(1L, "a@a.com", "password1", 0);
            Member memberB = new Member(2L, "b@b.com", "password2", 0);
            Product product = new Product("치킨", 10000, "http://chicken.com");
            OrderProduct orderProduct = new OrderProduct(product, 5);
            Order order = new Order(memberA, List.of(orderProduct), 0);

            assertThatThrownBy(() -> order.checkOwner(memberB))
                    .isInstanceOf(OrderException.class)
                    .hasMessage("해당 주문을 관리할 수 있는 멤버가 아닙니다.");
        }
    }

    @Test
    @DisplayName("calculateTotalPrice 메서드는 주문 상품의 전체 가격을 계산한다.")
    void calculateTotalPrice() {
        Product productA = new Product("치킨", 10000, "http://chicken.com");
        Product productB = new Product("피자", 13000, "http://pizza.com");
        OrderProduct orderProductA = new OrderProduct(productA, 2);
        OrderProduct orderProductB = new OrderProduct(productB, 3);
        Order order = new Order(null, List.of(orderProductA, orderProductB), 0);

        int result = order.calculateTotalPrice();

        assertThat(result).isEqualTo(59000);
    }
}
