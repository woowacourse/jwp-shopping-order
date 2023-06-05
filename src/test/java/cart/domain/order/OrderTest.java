package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.member.Member;
import cart.domain.price.Price;
import cart.domain.product.Product;
import cart.exception.IllegalOrderException;
import cart.exception.NumberRangeException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    private final Product productA = new Product(1L, "상품A", 1000, "http://imageA.com");
    private final Product productB = new Product(2L, "상품B", 2000, "http://imageB.com");
    private final Member member = new Member(1L, "gray@google.com", "1234", 1000);
    private final OrderItem orderItemA = new OrderItem(1L, productA, 2, 2000);
    private final OrderItem orderItemB = new OrderItem(2L, productB,4, 8000);

    @Test
    void 주문이_정상적으로_생성된다() {
        Order order = new Order(1L, member, List.of(orderItemA, orderItemB), 500, LocalDateTime.now());

        assertAll(
                () -> assertThat(order.getId()).isEqualTo(1L),
                () -> assertThat(order.getMember()).isEqualTo(member),
                () -> assertThat(order.getOrderItems()).hasSize(2)
        );
    }

    @Test
    void 주문시_주문_상품이_없으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> new Order(null, member, Collections.emptyList(), 0, LocalDateTime.now()))
                .isInstanceOf(IllegalOrderException.class)
                .hasMessage("주문할 상품이 존재하지 않습니다.");
    }

    @Test
    void 포인트가_전체_금액보다_크면_예외가_발생한다() {
        assertThatThrownBy(() -> new Order(1L, member, List.of(orderItemA, orderItemB), 10001, LocalDateTime.now()))
                .isInstanceOf(NumberRangeException.class);
    }

    @Test
    void 주문의_총_가격을_계산한다() {
        Order order = new Order(1L, member, List.of(orderItemA, orderItemB), 500, LocalDateTime.now());

        Price totalPrice = order.calculateTotalPrice();

        assertThat(totalPrice.getAmount()).isEqualTo(10000);
    }

    @Test
    void 주문의_결제_가격을_계산한다() {
        Order order = new Order(1L, member, List.of(orderItemA, orderItemB), 500, LocalDateTime.now());

        Price spendPrice = order.calculateSpendPrice();

        assertThat(spendPrice.getAmount()).isEqualTo(9500);
    }

    @Test
    void 주문의_썸네일은_첫번째_상품의_이미지_주소다() {
        Order order = new Order(1L, member, List.of(orderItemA, orderItemB), 500, LocalDateTime.now());

        String thumbnailUrl = order.getThumbnailUrl();

        assertThat(thumbnailUrl).isEqualTo(orderItemA.getProduct().getImageUrl());
    }
}
