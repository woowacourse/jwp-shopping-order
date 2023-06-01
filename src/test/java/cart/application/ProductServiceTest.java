package cart.application;

import cart.dao.product.ProductDao;
import cart.dto.product.ProductResponse;
import cart.exception.customexception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void 모든_상품을_조회할_때_상품이_없으면_빈_리스트를_반환한다() {
        // given
        when(productDao.findAllProducts())
                .thenReturn(Collections.emptyList());

        // when
        List<ProductResponse> products = productService.getAllProducts();

        // then
        assertThat(products).isEmpty();
    }

    @Test
    void 존재하지않는_상품아이디로_상품을_조회할_때_예외를_던진다() {
        // given
        when(productDao.findProductById(anyLong()))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> productService.getProductById(1L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
