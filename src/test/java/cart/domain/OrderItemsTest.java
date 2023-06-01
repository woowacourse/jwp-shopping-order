package cart.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Order Items 단위 테스트")
class OrderItemsTest {

    private OrderItems orderItems;

    @BeforeEach
    void setUp() {
        final Product product1 = new Product(1L, "삼겹살", new Price(17000), "삼겹살.jpg");
        final Product product2 = new Product(2L, "목살", new Price(16000), "목살.jpg");
        final Product product3 = new Product(3L, "우대갈비", new Price(30000), "우대갈비.jpg");
        final List<OrderItem> orderItems = List.of(
                new OrderItem(product1, new Quantity(2)),
                new OrderItem(product2, new Quantity(1)),
                new OrderItem(product3, new Quantity(3)));
        this.orderItems = new OrderItems(orderItems, new DiscountPriceCalculator());
    }

    @Test
    @DisplayName("원래 가격 계산 테스트")
    void calculate_original_price_test() {
        // given, when
        final Price price = orderItems.calculateOriginalPrice();

        // then
        final Price expected = new Price(17000 * 2 + 16000 + 30000 * 3);
        assertThat(price).isEqualTo(expected);
    }

    @Test
    @DisplayName("할인 가격 계산 테스트")
    void calculate_discount_price_test() {
        // given, when
        final Price price = orderItems.calculateDiscountPrice();

        // then
        final Price expected = new Price(5000);
        assertThat(price).isEqualTo(expected);
    }

    @Test
    @DisplayName("지불 금액이 일치 여부 확인 테스트 - true")
    void payment_amount_is_equal() {
        // given
        final Price price = new Price(135000);

        // when
        final boolean result = orderItems.isPaymentAmountEqual(price);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("지불 금액이 일치 여부 확인 테스트 - false")
    void payment_amount_is_not_equal() {
        // given
        final Price invalidPrice = new Price(138000);

        // when
        final boolean result = orderItems.isPaymentAmountEqual(invalidPrice);

        // then
        assertThat(result).isFalse();
    }
}
