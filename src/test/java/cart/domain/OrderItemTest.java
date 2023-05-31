package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    @DisplayName("수량에 따른 상품의 가격을 계산할 수 있다.")
    void calculatePrice() {
        // given
        OrderItem orderItem = new OrderItem(new Product("치킨", Money.from(10_000), "tmpImg"),
            Quantity.from(2));

        // when
        Money price = orderItem.calculatePrice();

        // then
        assertThat(price).isEqualTo(Money.from(20_000));
    }
}
