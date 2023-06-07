package cart.domain;

import cart.util.CurrentTimeUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderEntityTest {

    @Test
    void ID가_같으면_같은_객체이다() {
        System.out.println(LocalDateTime.now());
        OrderEntity orderEntity = new OrderEntity(1L, CurrentTimeUtil.asString(), 1L, 1, 1, 1, 1);
        OrderEntity sameExpect = new OrderEntity(1L, CurrentTimeUtil.asString(), 1L, 1, 1, 1, 1);
        OrderEntity differentExpect = new OrderEntity(2L, CurrentTimeUtil.asString(), 1L, 1, 1, 1, 1);

        assertThat(orderEntity).isEqualTo(sameExpect);
        assertThat(orderEntity).isNotEqualTo(differentExpect);
    }
}
