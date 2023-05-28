package cart.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.common.Money;
import cart.domain.member.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderItemTest {

    @Test
    void 가격의_총합을_계산하여_반환한다() {
        // given
        final Member member = new Member("pizza@pizza.com", "password");
        final Product product = new Product("pizza1", "pizza1.jpg", 8900L);
        final Item orderItem = new OrderItem(null, 3, member, product);

        // expect
        assertThat(orderItem.calculateTotalPrice()).isEqualTo(Money.from(26700));
    }
}
