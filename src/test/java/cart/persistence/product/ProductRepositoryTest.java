package cart.persistence.product;

import cart.application.repository.product.ProductRepository;
import cart.domain.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
class ProductRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductJdbcRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("상품을 올바르게 생성한다.")
    void createProductTest() {
        //given
        final Product product = 배변패드;
        //when,then
        assertDoesNotThrow(() -> productRepository.createProduct(product));
    }

    @Test
    @DisplayName("전체 상품 목록을 조회한다")
    void findAllProductsTest() {
        // given
        final Long savedPadId = productRepository.createProduct(배변패드);
        final Long savedTailId = productRepository.createProduct(꼬리요리);
        final Long savedDinoId = productRepository.createProduct(통구이);

        final Product savedPad = new Product(savedPadId, 배변패드.getName(), 배변패드.getPrice(), 배변패드.getImageUrl());
        final Product savedTail = new Product(savedTailId, 꼬리요리.getName(), 꼬리요리.getPrice(), 꼬리요리.getImageUrl());
        final Product savedDino = new Product(savedDinoId, 통구이.getName(), 통구이.getPrice(), 통구이.getImageUrl());

        // when
        final List<Product> products = productRepository.findAll();

        // then
        assertThat(products).usingRecursiveComparison().isEqualTo(List.of(savedPad, savedTail, savedDino));
    }

    @Test
    @DisplayName("상품 id로 특정 상품 정보를 조회한다.")
    void findProductByIdTest() {
        // given
        final Long savedPadId = productRepository.createProduct(배변패드);
        final Long savedTailId = productRepository.createProduct(꼬리요리);

        final Product savedPad = new Product(savedPadId, 배변패드.getName(), 배변패드.getPrice(), 배변패드.getImageUrl());
        final Product savedTail = new Product(savedTailId, 꼬리요리.getName(), 꼬리요리.getPrice(), 꼬리요리.getImageUrl());

        // when
        final Optional<Product> product = productRepository.findById(savedTailId);

        // then
        assertThat(product.get()).usingRecursiveComparison().isEqualTo(savedTail);
    }

    @Test
    @DisplayName("존재하지 않는 상품id로 조회 시 예외 처리한다")
    void findProductById_EmptyOptionalTest() {
        Assertions.assertThat(productRepository.findById(2L)).isEmpty();
    }

    @Test
    @DisplayName("상품 정보를 변경한다.")
    void updateProductTest() {

        // given
        final Long savedPadId = productRepository.createProduct(배변패드);

        final Product changeProduct = new Product(savedPadId, 꼬리요리.getName(), 꼬리요리.getPrice(), 꼬리요리.getImageUrl());

        // when
        productRepository.updateProduct(changeProduct);

        // then
        final Optional<Product> product = productRepository.findById(savedPadId);
        assertThat(product.get()).usingRecursiveComparison().isEqualTo(changeProduct);
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void deleteProductTest() {
        // given
        final Long savedPadId = productRepository.createProduct(배변패드);
        final Long savedTailId = productRepository.createProduct(꼬리요리);

        // when
        productRepository.deleteProduct(savedPadId);

        // then
        assertThat(productRepository.findAll()).hasSize(1);
    }

}
