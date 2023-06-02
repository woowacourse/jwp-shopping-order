package cart.service;

import cart.domain.Money;
import cart.domain.Product;
import cart.dto.ProductResponse;
import cart.dto.ProductSaveRequest;
import cart.dto.ProductUpdateRequest;
import cart.exception.ProductNotFoundException;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품을_저장한다() {
        // given
        final ProductSaveRequest request = new ProductSaveRequest("허브티", "tea.jpg", BigDecimal.valueOf(1000L));

        // when
        final Long id = productService.save(request);

        // then
        final List<Product> result = productRepository.findAll();
        assertAll(
                () -> assertThat(id).isPositive(),
                () -> assertThat(result).hasSize(1)
        );
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        final ProductSaveRequest request1 = new ProductSaveRequest("허브티", "tea.jpg", BigDecimal.valueOf(99L));
        final ProductSaveRequest request2 = new ProductSaveRequest("블랙켓티", "bk_tea.jpg", BigDecimal.valueOf(100L));
        final Long id1 = productService.save(request1);
        final Long id2 = productService.save(request2);

        // when
        final List<ProductResponse> result = productService.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new ProductResponse(id1, request1.getName(), request1.getImageUrl(), request1.getPrice()),
                new ProductResponse(id2, request2.getName(), request2.getImageUrl(), request2.getPrice())
        ));
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        final ProductSaveRequest product = new ProductSaveRequest("허브티", "tea.jpg", BigDecimal.valueOf(99L));
        final Long id = productService.save(product);

        // when
        final ProductResponse productDto = productService.findById(id);

        // then
        assertAll(
                () -> assertThat(productDto.getId()).isEqualTo(id),
                () -> assertThat(productDto.getName()).isEqualTo("허브티"),
                () -> assertThat(productDto.getImageUrl()).isEqualTo("tea.jpg"),
                () -> assertThat(productDto.getPrice().longValue()).isEqualTo(99L)
        );
    }

    @Test
    void 없는_상품을_조회하는_경우_ProductNotFoundException_을_던진다() {
        // expect
        assertThatThrownBy(() -> productService.findById(Long.MAX_VALUE))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("상품을 찾을 수 없습니다.");
    }

    @Test
    void 상품을_수정한다() {
        // given
        final ProductSaveRequest product = new ProductSaveRequest("허브티", "tea.jpg", BigDecimal.valueOf(99L));
        final Long id = productService.save(product);
        final ProductUpdateRequest request = new ProductUpdateRequest("블랙캣", "cat.jpg", BigDecimal.valueOf(100L));

        // when
        productService.update(id, request);

        // then
        final Product result = productRepository.findAll().get(0);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getName()).isEqualTo("블랙캣"),
                () -> assertThat(result.getImageUrl()).isEqualTo("cat.jpg"),
                () -> assertThat(result.getPrice()).isEqualTo(new Money(100L))
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        final ProductSaveRequest product = new ProductSaveRequest("허브티", "tea.jpg", BigDecimal.valueOf(99L));
        final Long id = productService.save(product);

        // when
        productService.delete(id);

        // then
        assertThat(productRepository.findAll()).isEmpty();
    }
}
