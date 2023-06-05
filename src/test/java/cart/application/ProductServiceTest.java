package cart.application;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.SALAD;
import static cart.fixture.DtoFixture.CHICKEN_PRODUCT_REQUEST;
import static cart.fixture.RepositoryFixture.productRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import cart.dto.response.ProductResponse;
import cart.exception.ProductException;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ProductServiceTest {

    ProductRepository productRepository;
    ProductService productService;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        productRepository = productRepository(jdbcTemplate);
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("save는 저장할 상품에 대한 정보를 전달하면 해당 정보의 상품을 저장하고 ID를 반환한다.")
    void saveSuccessTest() {
        Long actual = productService.save(CHICKEN_PRODUCT_REQUEST);

        assertThat(actual).isPositive();
    }

    @Test
    @DisplayName("findAll은 호출하면 모든 상품을 반환한다.")
    void findAllTest() {
        List<ProductResponse> actual = productService.findAll();

        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("findById는 존재하는 상품 ID를 호출하면 해당 상품을 반환한다.")
    void findByIdSuccessTest() {
        ProductResponse actual = productService.findById(CHICKEN.getId());

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(CHICKEN.getId()),
                () -> assertThat(actual.getName()).isEqualTo(CHICKEN.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(CHICKEN.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(CHICKEN.getImageUrl())
        );
    }

    @Test
    @DisplayName("findById는 존재하지 않는 상품 ID를 호출하면 예외가 발생한다.")
    void findByIdFailTest() {
        assertThatThrownBy(() -> productService.findById(-999L))
                .isInstanceOf(ProductException.NotFound.class)
                .hasMessageContaining("해당 상품을 찾을 수 없습니다 : ");
    }

    @Test
    @DisplayName("update는 존재하는 상품 ID와 변경할 상품 정보를 전달하면 해당 상품을 변경한다.")
    void updateSuccessTest() {
        productService.update(SALAD.getId(), CHICKEN_PRODUCT_REQUEST);

        Product actual = productRepository.findById(SALAD.getId()).get();
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(SALAD.getId()),
                () -> assertThat(actual.getName()).isEqualTo(CHICKEN.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(CHICKEN.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(CHICKEN.getImageUrl())
        );
    }

    @Test
    @DisplayName("update는 존재하지 않는 상품 ID를 전달하면 예외가 발생한다.")
    void updateFailTest() {
        assertThatThrownBy(() -> productService.update(-999L, CHICKEN_PRODUCT_REQUEST))
                .isInstanceOf(ProductException.NotFound.class)
                .hasMessageContaining("해당 상품을 찾을 수 없습니다 : ");
    }
}
