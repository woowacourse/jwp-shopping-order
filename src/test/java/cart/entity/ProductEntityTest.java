package cart.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import cart.domain.ProductStock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductEntityTest {

    @DisplayName("id와 다른 ProductEntity 로 새로운 객체를 생성한다.")
    @Test
    void constructWithOther() {
        final ProductEntity entity = new ProductEntity(1L, "a", 1, "b", 2);
        final ProductEntity result = new ProductEntity(2L, entity);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(2L),
                () -> assertThat(result.getName()).isEqualTo("a"),
                () -> assertThat(result.getPrice()).isEqualTo(1),
                () -> assertThat(result.getImageUrl()).isEqualTo("b"),
                () -> assertThat(result.getStock()).isEqualTo(2)
        );
    }

    @DisplayName("id 없이 새로운 객체를 생성한다.")
    @Test
    void constructWithNoName() {
        final ProductEntity result = new ProductEntity("a", 1, "b", 2);
        assertAll(
                () -> assertThat(result.getId()).isNull(),
                () -> assertThat(result.getName()).isEqualTo("a"),
                () -> assertThat(result.getPrice()).isEqualTo(1),
                () -> assertThat(result.getImageUrl()).isEqualTo("b"),
                () -> assertThat(result.getStock()).isEqualTo(2)
        );
    }

    @DisplayName("새로운 객체를 생성한다.")
    @Test
    void construct() {
        final ProductEntity result = new ProductEntity(1L, "a", 1, "b", 2);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo("a"),
                () -> assertThat(result.getPrice()).isEqualTo(1),
                () -> assertThat(result.getImageUrl()).isEqualTo("b"),
                () -> assertThat(result.getStock()).isEqualTo(2)
        );
    }

    @DisplayName("ProductStock 객체를 반환한다.")
    @Test
    void toProductStock() {
        final ProductEntity entity = new ProductEntity(1L, "a", 1, "b", 2);
        final ProductStock result = entity.toProductStock();
        assertAll(
                () -> assertThat(result.getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.getProduct().getName()).isEqualTo("a"),
                () -> assertThat(result.getProduct().getPrice()).isEqualTo(1),
                () -> assertThat(result.getProduct().getImageUrl()).isEqualTo("b"),
                () -> assertThat(result.getStock().getValue()).isEqualTo(2)
        );
    }

    @DisplayName("Product 객체를 반환한다.")
    @Test
    void toProduct() {
        final ProductEntity entity = new ProductEntity(1L, "a", 1, "b", 2);
        final Product result = entity.toProduct();
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo("a"),
                () -> assertThat(result.getPrice()).isEqualTo(1),
                () -> assertThat(result.getImageUrl()).isEqualTo("b")
        );
    }
}
