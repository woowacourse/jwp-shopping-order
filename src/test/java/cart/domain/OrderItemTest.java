package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderItemTest {

    private OrderItem orderItem;

    @DisplayName("할인율이 0이면 멤버할인이라는 것에 대한 true를 반환한다.")
    @Test
    void isMemberDiscountTrue() {
        int discountRate = 0;
        orderItem = new OrderItem("치킨", 10_000, "image.url", 3, discountRate);

        // when, then
        assertThat(orderItem.isMemberDiscount()).isTrue();
    }

    @DisplayName("할인율이 0이 아니면 상품할인이라는 것에 대한 true를 반환한다.")
    @Test
    void isMemberDiscountFalse() {
        //given
        int discountRate = 10;
        orderItem = new OrderItem("치킨", 10_000, "image.url", 3, discountRate);

        // when, then
        assertThat(orderItem.isProductDiscount()).isTrue();
    }

    @DisplayName("상품 할인에 대한 할인한 금액을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:10:3_000", "4:20:8_000", "1:15:1_500"}, delimiter = ':')
    void calculateDiscountAmount(int quantity, int discountRate, int expectedAmount) {
        //given
        orderItem = new OrderItem("치킨", 10_000, "image.url", quantity, discountRate);

        // when
        int discountAmount = orderItem.calculateDiscountAmount();

        // then
        assertThat(discountAmount).isEqualTo(expectedAmount);
    }

    @DisplayName("받은 멤버 할인률에 따른 할인한 금액을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:0.1:3_000", "4:0.2:8_000", "1:0.15:1_500"}, delimiter = ':')
    void calculateMemberDiscountAmount(int quantity, double memberDiscountRate, int expectedAmount) {
        //given
        orderItem = new OrderItem("치킨", 10_000, "image.url", quantity, 0);

        // when
        int discountAmount = orderItem.calculateMemberDiscountAmount(memberDiscountRate);

        // then
        assertThat(discountAmount).isEqualTo(expectedAmount);
    }

    @DisplayName("받은 할인율로 계산된 금액을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:0.1:27_000", "4:0.2:32_000", "1:0.15:8_500"}, delimiter = ':')
    void calculateDiscountedPriceByGrade(int quantity, double discountRate, int expectedAmount) {
        //given
        orderItem = new OrderItem("치킨", 10_000, "image.url", quantity, 0);

        // when
        int discountAmount = orderItem.calculateDiscountedPriceByGrade(discountRate);

        // then
        assertThat(discountAmount).isEqualTo(expectedAmount);
    }

    @DisplayName("상품할인으로 계산된 최종금액을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:10:27_000", "4:20:32_000", "1:15:8_500"}, delimiter = ':')
    void calculateDiscountedPrice(int quantity, int discountRate, int expectedAmount) {
        //given
        orderItem = new OrderItem("치킨", 10_000, "image.url", quantity, discountRate);

        // when
        int discountAmount = orderItem.calculateDiscountedPrice();

        // then
        assertThat(discountAmount).isEqualTo(expectedAmount);
    }
}
