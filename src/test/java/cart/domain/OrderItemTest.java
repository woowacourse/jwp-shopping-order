package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderItem 단위테스트")
class OrderItemTest {

    @Test
    void orderitem은_가격을_계산할_수_있다() {
        // given
        Product chicken = new Product("치킨", 5000, "http://chickenimageutl", 30);

        // when
        OrderItem orderItem = OrderItem.of(chicken, 3);

        // then
        Assertions.assertThat(orderItem.calculatePrice()).isEqualTo(15000);
    }
}
