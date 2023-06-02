package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderItemEntityTest {

    @Test
    void ID가_같으면_같은_객체이다() {
        OrderItemEntity orderItemEntity = new OrderItemEntity(1L, 22L, 30L, "name", 1_000, "image", 10);
        OrderItemEntity same = new OrderItemEntity(1L, 22L, 30L, "name", 1_000, "image", 10);
        OrderItemEntity different = new OrderItemEntity(2L, 22L, 30L, "name", 1_000, "image", 10);

        assertThat(orderItemEntity).isEqualTo(same);
        assertThat(orderItemEntity).isNotEqualTo(different);
    }

    @Test
    void ID가_같으면_같은_해시값을_가진다() {
        OrderItemEntity orderItemEntity = new OrderItemEntity(1L, 22L, 30L, "name", 1_000, "image", 10);
        OrderItemEntity same = new OrderItemEntity(1L, 22L, 30L, "name", 1_000, "image", 10);
        OrderItemEntity different = new OrderItemEntity(2L, 22L, 30L, "name", 1_000, "image", 10);

        assertThat(orderItemEntity.hashCode()).isEqualTo(same.hashCode());
        assertThat(orderItemEntity.hashCode()).isNotEqualTo(different.hashCode());
    }
}
