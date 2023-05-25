package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.exception.InvalidOrderException;
import cart.fixture.Fixture;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문을 한다.")
    public void order() {
        // given
        List<Long> cartItemIds = Arrays.asList(1L, 2L);
        Member member = Fixture.GOLD_MEMBER;
        OrderRequest orderRequest = new OrderRequest(cartItemIds);

        // when
        Long result = orderService.order(orderRequest, member);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("cartItem의 member와 전달받은 member가 일치하지 않으면 예외를 던진다.")
    public void orderFail() {
        // given
        List<Long> cartItemIds = Arrays.asList(1L, 2L, 3L);
        Member member = Fixture.GOLD_MEMBER;
        OrderRequest orderRequest = new OrderRequest(cartItemIds);

        // then
        assertThatThrownBy(() -> orderService.order(orderRequest, member))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("Some of cart items doesn't belong to member.");
    }
}
