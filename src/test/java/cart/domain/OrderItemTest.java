package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    @DisplayName("수량에 따른 상품의 가격을 계산할 수 있다.")
    void calculatePrice() {
        // given
        OrderItem orderItem = new OrderItem(new OrderProduct("치킨", Money.from(10_000), "tmpImg"),
            Quantity.from(2));

        // when
        Money price = orderItem.calculatePrice();

        // then
        assertThat(price).isEqualTo(Money.from(20_000));
    }

    @Test
    @DisplayName("같은 id를 가진 orderItem이라면 같은 객체로 취급된다.")
    void sameId_sameOrderItem() {
        // given
        OrderItem orderItem1 = new OrderItem(1L, new OrderProduct("name", Money.from(5000), "img"),
            Quantity.DEFAULT);
        OrderItem orderItem2 = new OrderItem(1L, new OrderProduct("name2", Money.from(5000), "img"),
            Quantity.DEFAULT);

        // when, then
        assertThat(orderItem1).isEqualTo(orderItem2);
    }

}
