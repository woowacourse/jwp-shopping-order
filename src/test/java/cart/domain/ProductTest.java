package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 모든_필드가_같으면_같은_객체이다() {
        // given
        Product product1 = new Product(1L, "name", 1000, "imageUrl", 10);
        Product product2 = new Product(1L, "name", 1000, "imageUrl", 10);

        // expect
        assertThat(product1).isEqualTo(product2);
    }
}
