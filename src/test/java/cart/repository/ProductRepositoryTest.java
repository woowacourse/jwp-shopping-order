package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import cart.domain.common.Money;
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
        final Product product = new Product("허브티", "tea.jpg", 1000L);

        // when
        productRepository.save(product);

        // then
        assertThat(productRepository.findAll()).hasSize(1);
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        final Product product1 = productRepository.save(new Product("허브티", "tea.jpg", 1000L));
        final Product product2 = productRepository.save(new Product("고양이", "cat.jpg", 1000000L));

        // when
        List<Product> result = productRepository.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(product1, product2));
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        final Product product = productRepository.save(new Product("허브티", "tea.jpg", 1000L));

        // when
        final Product result = productRepository.findById(product.getId()).get();

        // then
        assertThat(result).isEqualTo(product);
    }

    @Test
    void 상품을_수정한다() {
        // given
        final Product product = productRepository.save(new Product("허브티", "tea.jpg", 1000L));
        final Product updatedProduct = new Product(product.getId(), "블랙캣", "cat.jpg", 10000L);

        // when
        productRepository.save(updatedProduct);

        // then
        final Product result = productRepository.findById(updatedProduct.getId()).get();
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("블랙캣"),
                () -> assertThat(result.getImageUrl()).isEqualTo("cat.jpg"),
                () -> assertThat(result.getPrice()).isEqualTo(Money.from(10000L))
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        final Product product = productRepository.save(new Product("허브티", "tea.jpg", 1000L));

        // when
        productRepository.deleteById(product.getId());

        // then
        assertThat(productRepository.findAll()).isEmpty();
    }
}
