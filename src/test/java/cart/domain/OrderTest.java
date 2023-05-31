package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource(value = {"10,500", "1,50", "0.1,5"})
    void 결제_금액의_일정_비율만큼_포인트가_계산된다(double percent, long point) {
        Order order = new Order(1L, member, List.of(orderItemA, orderItemB), 5000, LocalDateTime.now());

        Point rewardPoint = order.calculateRewardPoint(percent);

        assertThat(rewardPoint.getAmount()).isEqualTo(point);
    }

    @Test
    void 결제_금액이_0원이면_적립_포인트가_0원이어야_한다() {
        Order order = new Order(1L, member, List.of(orderItemA, orderItemB), 10000, LocalDateTime.now());

        Point rewardPoint = order.calculateRewardPoint(1);

        assertThat(rewardPoint.getAmount())
                .isZero();
    }
}
