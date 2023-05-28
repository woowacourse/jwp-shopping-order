package cart.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.ProductEntity;
import cart.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductMapperTest {

    @Test
    @DisplayName("엔티티를 도메인으로 변환한다.")
    void convertFromEntityToDomain() {
        ProductEntity entity = new ProductEntity(1L, "치킨", 13000, "http://chicken.com");

        Product result = ProductMapper.toDomain(entity);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(entity.getId()),
                () -> assertThat(result.getName()).isEqualTo(entity.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(entity.getPrice()),
                () -> assertThat(result.getImageUrl()).isEqualTo(entity.getImageUrl())
        );
    }

    @Test
    @DisplayName("도메인을 엔티티로 변환한다.")
    void convertFromDomainToEntity() {
        Product domain = new Product(1L, "치킨", 13000, "http://chicken.com");

        ProductEntity result = ProductMapper.toEntity(domain);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(domain.getId()),
                () -> assertThat(result.getName()).isEqualTo(domain.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(domain.getPrice()),
                () -> assertThat(result.getImageUrl()).isEqualTo(domain.getImageUrl()),
                () -> assertThat(result.getCreatedAt()).isNull(),
                () -> assertThat(result.getUpdatedAt()).isNull()
        );
    }
}
