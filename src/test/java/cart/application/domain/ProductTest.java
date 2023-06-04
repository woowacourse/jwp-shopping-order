package cart.application.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("포인트 사용가능 상품이라면, 상품 가격만큼 사용할 수 있다")
    void calculateAvailablePoint() {
        // given
        Product product = new Product(0L, "", 1000, "", 0.0, true);
        // when
        long point = product.calculateAvailablePoint();
        // then
        assertThat(point).isEqualTo(1000);
    }

    @Test
    @DisplayName("포인트 사용 불가능 상품이라면, 0원을 반환한다")
    void calculateAvailablePoint_notAvailable() {
        // given
        Product product = new Product(0L, "", 1000, "", 0.0, false);
        // when
        long point = product.calculateAvailablePoint();
        // then
        assertThat(point).isEqualTo(0);
    }

    @Test
    @DisplayName("적립될 포인트를 계산할 수 있다")
    void calculatePointToEarn() {
        // given
        Product product = new Product(0L, "", 1000, "", 10.0, false);
        // when
        long earned = product.calculatePointToEarn();
        // then
        assertThat(earned).isEqualTo(100L);
    }
}
