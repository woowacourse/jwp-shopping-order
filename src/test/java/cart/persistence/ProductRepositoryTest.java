package cart.persistence;

import cart.persistence.product.JdbcTemplateProductDao;
import cart.domain.product.ProductRepository;
import cart.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
public class ProductRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new JdbcTemplateProductDao(jdbcTemplate);
    }

    @Test
    void 상품을_추가하고_조회한다() {
        // given, when
        Long productId = productRepository.createProduct(치킨);
        Product newProduct = new Product(
                productId,
                치킨.getName(),
                치킨.getPrice(),
                치킨.getImageUrl(),
                치킨.getStock());

        // then
        assertThat(productRepository.findProductById(productId).get())
                .isEqualTo(newProduct);
    }

    @Test
    void 없는_상품을_조회하면_빈_옵셔널을_반환한다() {
        // given
        Long productId = productRepository.createProduct(치킨);

        // when
        Optional<Product> product = productRepository.findProductById(productId + 1);

        // then
        assertThat(product.isEmpty()).isTrue();
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        productRepository.createProduct(치킨);
        productRepository.createProduct(피자);
        productRepository.createProduct(샐러드);

        // when
        List<Product> products = productRepository.findAllProducts();

        // then
        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    void 전체_상품을_조회할_때_상품이_없으면_빈_리스트를_반환한다() {
        // given, when
        List<Product> products = productRepository.findAllProducts();

        // then
        assertThat(products).isEmpty();
    }

    @Test
    void 상품을_업데이트한다() {
        // given
        Long productId = productRepository.createProduct(치킨);

        // when
        productRepository.updateProduct(productId, 피자);

        // then
        assertThat(productRepository.findProductById(productId).get().getName())
                .isEqualTo(피자.getName());
    }

    @Test
    void 상품의_수량을_변경한다() {
        // given
        Long productId = productRepository.createProduct(치킨);

        // when
        productRepository.updateStock(productId, 치킨.getStock() - 3L);

        // then
        assertThat(productRepository.findProductById(productId).get().getStock())
                .isEqualTo(치킨.getStock() - 3L);
    }

    @Test
    void 상품의_수량을_0미만으로_변경하면_예외가_발생한다() {
        // given
        Long productId = productRepository.createProduct(치킨);

        // when, then

        assertThatThrownBy(() -> productRepository.updateStock(productId, 치킨.getStock() - 11L));
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Long productId = productRepository.createProduct(치킨);

        // when
        productRepository.deleteProduct(productId);

        // then
        assertThat(productRepository.findProductById(productId).isEmpty())
                .isTrue();
    }
}
