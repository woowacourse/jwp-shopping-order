package cart.service;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ProductException;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void 전체_상품을_조회한다() {
        // given
        List<Product> value = List.of(
                new Product(1L, "밀리", valueOf(10000000), "http://millie.com"),
                new Product(2L, "박스터", valueOf(1000), "http://boxster.com"),
                new Product(3L, "햄스터", valueOf(100000), "http://hamster.com")
        );
        given(productRepository.findAll())
                .willReturn(value);

        // when
        List<ProductResponse> responses = productService.findAll();

        // then
        assertThat(responses).map(ProductResponse::getId)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    void 상품의_id가_없는_경우_예외가_발생한다() {
        // given
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(ProductException.class);
    }

    @Test
    void 상품의_id로_조회한다() {
        // given
        given(productRepository.findById(1L))
                .willReturn(Optional.of(new Product(1L, "밀리", valueOf(10000000), "http://millie.com")));

        // when
        ProductResponse response = productService.findById(1L);

        // then
        assertThat(response.getName()).isEqualTo("밀리");
    }

    @Test
    void 상품을_등록한다() {
        // given
        given(productRepository.save(any()))
                .willReturn(new Product(1L, "밀리", valueOf(10000000), "http://millie.com"));

        // when
        Long id = productService.create(new ProductRequest("밀리", valueOf(10000000), "http://millie.com"));

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 수정할_상품이_없는_경우_예외가_발생한다() {
        // given
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> productService.updateProduct(
                1L,
                new ProductRequest("밀리", valueOf(10000000), "http://millie.com")
        )).isInstanceOf(ProductException.class);
    }

    @Test
    void 상품을_수정한다() {
        // given
        Product product = new Product(1L, "밀리", valueOf(10000000), "http://millie.com");
        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));

        // when
        productService.updateProduct(1L, new ProductRequest("박스터", valueOf(10), "http://boxster.com"));

        // then
        verify(productRepository, times(1)).update(any());
        assertAll(
                () -> assertThat(product.getPrice().getValue()).isEqualTo(valueOf(10)),
                () -> assertThat(product.getName()).isEqualTo("박스터"),
                () -> assertThat(product.getImageUrl()).isEqualTo("http://boxster.com")
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Product product = new Product(1L, "밀리", valueOf(10000000), "http://millie.com");

        // when
        productService.deleteById(product.getId());

        // then
        verify(productRepository, times(1)).deleteProduct(product.getId());
    }
}
