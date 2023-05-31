package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CartItemEntityTest {

    @Test
    void ID가_같으면_같은_객체이다() {
        // given
        CartItemEntity entity = new CartItemEntity(1L, 1L, 1L, 1);
        CartItemEntity sameEntity = new CartItemEntity(1L, 1L, 1L, 1);
        CartItemEntity differentEntity = new CartItemEntity(2L, 2L, 2L, 2);

        // expect
        assertThat(entity).isEqualTo(sameEntity);
        assertThat(entity).isNotEqualTo(differentEntity);
    }

    @Test
    void ID가_같으면_같은_해시코드를_가진다() {
        // given
        CartItemEntity entity = new CartItemEntity(1L, 1L, 1L, 1);
        CartItemEntity sameEntity = new CartItemEntity(1L, 1L, 1L, 1);
        CartItemEntity differentEntity = new CartItemEntity(2L, 2L, 2L, 2);

        // expect
        assertThat(entity.hashCode()).isEqualTo(sameEntity.hashCode());
        assertThat(entity.hashCode()).isNotEqualTo(differentEntity.hashCode());
    }
}
