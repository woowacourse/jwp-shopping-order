package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.domain.Product;
import cart.exception.ErrorMessage;
import cart.exception.ProductException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class ProductRepositoryTest {

    private final ProductEntity 첫번째_상품_엔티티 = new ProductEntity(null, "치킨", 10_000, "http://example/chicken.png");
    private final ProductEntity 두번째_상품_엔티티 = new ProductEntity(null, "피자", 20_000, "http://example/chicken.png");

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductRepository productRepository;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
        productRepository = new ProductRepository(productDao);
    }

    @Test
    void 상품을_수정한다() {
        // given
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Product 변경될_상품 = new Product(상품.getId(), 상품.getName(), 20000, 상품.getImageUrl());

        // when
        productRepository.update(상품.getId(), 변경될_상품);
        Product 저장된_상품 = productRepository.findById(상품.getId());

        // then
        assertThat(저장된_상품.getPrice()).isEqualTo(상품.getPrice() + 10000);
    }

    private Product 상품을_저장하고_ID를_갖는_상품을_리턴한다(ProductEntity 상품_엔티티) {
        Long 저장된_상품_ID = productDao.save(상품_엔티티);
        return new Product(저장된_상품_ID, 상품_엔티티.getName(), 상품_엔티티.getPrice(), 상품_엔티티.getImageUrl());
    }

    @Test
    void 없는_상품을_수정하려하면_예외를_반환한다() {
        // given
        Product 변경될_상품 = new Product(Long.MAX_VALUE, "변경된 치킨", 20000, "변경된 이미지");

        // then
        assertThatThrownBy(() -> productRepository.update(Long.MAX_VALUE, 변경될_상품))
                .isInstanceOf(ProductException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_PRODUCT.getMessage());
    }

    @Test
    void 없는_ID로_상품을_조회하려하면_예외를_반환한다() {
        // then
        assertThatThrownBy(() -> productRepository.findById(Long.MAX_VALUE))
                .isInstanceOf(ProductException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_PRODUCT.getMessage());
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        Product 첫번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Product 두번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);

        // when
        List<Product> 상품들 = productRepository.findAll();

        // then
        assertAll(
                () -> assertThat(상품들).hasSize(2),
                () -> assertThat(상품들).usingRecursiveComparison()
                        .isEqualTo(List.of(첫번째_상품, 두번째_상품))
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);

        // when
        productRepository.deleteById(상품.getId());

        // then
        assertThatThrownBy(() -> productRepository.findById(상품.getId()))
                .isInstanceOf(ProductException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_PRODUCT.getMessage());
    }

    @Test
    void 없는_ID로_상품을_삭제하려하면_예외를_반환한다() {
        // then
        assertThatThrownBy(() -> productRepository.deleteById(Long.MAX_VALUE))
                .isInstanceOf(ProductException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_PRODUCT.getMessage());
    }
}
