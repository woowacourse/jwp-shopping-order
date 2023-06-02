package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cart.util.CurrentTimeUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class OrderEntityTest {

    @Test
    void ID가_같으면_같은_객체이다() {
        System.out.println(LocalDateTime.now());
        OrderEntity orderEntity = new OrderEntity(1L, CurrentTimeUtil.asString(), 1L, 1L, "name", 1, "image", 1, 1, 1, 1, 1);
        OrderEntity sameExpect = new OrderEntity(1L, CurrentTimeUtil.asString(), 1L, 1L, "name", 1, "image", 1, 1, 1, 1, 1);
        OrderEntity differentExpect = new OrderEntity(2L, CurrentTimeUtil.asString(), 1L, 1L, "name", 1, "image", 1, 1, 1, 1, 1);

        assertThat(orderEntity).isEqualTo(sameExpect);
        assertThat(orderEntity).isNotEqualTo(differentExpect);
    }
}
