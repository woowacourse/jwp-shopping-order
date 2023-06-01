package cart.domain;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.상품_치킨_10개;
import static cart.fixture.TestFixture.상품_피자_1개;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    /*
        치킨 = 10000원
        피자 = 20000원
     */
    @Test
    void 주문_전체_금액을_계산한다() {
        Order order = new Order(밀리, List.of(상품_치킨_10개, 상품_피자_1개), 3000);

        Money totalPrice = order.calculateTotalPrice();

        assertThat(totalPrice).isEqualTo(new Money(123000));
    }
}
