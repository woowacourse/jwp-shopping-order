package cart.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void 인출한_금액과_포인트를_합친_지불금액이_총_가격과_같으면_주문이_생성된다() {
        final List<OrderDetail> orderDetails = List.of(new OrderDetail(new Product("참치", 1000, "이미지"), 1L),
                new OrderDetail(new Product("김치", 2000, "이미지"), 2L));
        final Payment payment = new Payment(BigDecimal.valueOf(4000));
        final Point point = new Point(BigDecimal.valueOf(1000));
        final Member member = new Member(1L, "이메일", "비밀번호");

        assertThatCode(
                () -> new Order(orderDetails, payment, point, member)
        ).doesNotThrowAnyException();
    }

    @Test
    void 인출한_금액과_포인트를_합친_지불금액이_총_가격과_다르면_예외가_발생한다() {
        final List<OrderDetail> orderDetails = List.of(new OrderDetail(new Product("참치", 1000, "이미지"), 1L),
                new OrderDetail(new Product("김치", 2000, "이미지"), 2L));
        final Payment payment = new Payment(BigDecimal.valueOf(4000));
        final Point point = new Point(BigDecimal.valueOf(500));
        final Member member = new Member(1L, "이메일", "비밀번호");

        assertThatThrownBy(
                () -> new Order(orderDetails, payment, point, member)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("결제금액과 총 가격이 일치하지 않습니다");
    }

}