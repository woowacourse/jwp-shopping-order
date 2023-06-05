package cart.repository;

import static cart.fixture.ProductFixture.상품_18900원;
import static cart.fixture.ProductFixture.상품_8900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.VO.Money;
import cart.domain.cart.Product;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품을_저장한다() {
        // given
        final Product product = 상품_8900원;

        // when
        productRepository.save(product);

        // then
        assertThat(productRepository.findAll()).hasSize(1);
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_18900원);

        // when
        List<Product> result = productRepository.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(product1, product2));
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        final Product product = productRepository.save(상품_8900원);

        // when
        final Product result = productRepository.findById(product.getId()).get();

        // then
        assertThat(result).isEqualTo(product);
    }

    @Test
    void 상품을_수정한다() {
        // given
        final Product product = productRepository.save(상품_8900원);
        final Product updatedProduct = new Product(product.getId(), "피자", "pizza.png", 10000L);

        // when
        productRepository.save(updatedProduct);

        // then
        final Product result = productRepository.findById(updatedProduct.getId()).get();
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("피자"),
                () -> assertThat(result.getImageUrl()).isEqualTo("pizza.png"),
                () -> assertThat(result.getPrice()).isEqualTo(Money.from(10000L))
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        final Product product = productRepository.save(상품_8900원);

        // when
        productRepository.deleteById(product.getId());

        // then
        assertThat(productRepository.findAll()).isEmpty();
    }
}
