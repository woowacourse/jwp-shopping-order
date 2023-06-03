package cart.service;

import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.dto.PageInfo;
import cart.dto.PageRequest;
import cart.dto.PagingProductResponse;
import cart.dto.ProductResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Test
    void 모든_상품을_페이징해서_얻는다() {
        //given
        final PageRequest pageRequest = new PageRequest(1, 10);

        when(productDao.getAllProductsBy(0, 10))
                .thenReturn(List.of(
                        new Product(1L, "1", 1000, "image.jpeg"),
                        new Product(2L, "2", 1000, "image.jpeg"),
                        new Product(3L, "3", 1000, "image.jpeg"),
                        new Product(4L, "4", 1000, "image.jpeg"),
                        new Product(5L, "5", 1000, "image.jpeg"),
                        new Product(6L, "6", 1000, "image.jpeg"),
                        new Product(7L, "7", 1000, "image.jpeg"),
                        new Product(8L, "8", 1000, "image.jpeg"),
                        new Product(9L, "9", 1000, "image.jpeg"),
                        new Product(10L, "10", 1000, "image.jpeg")
                ));

        when(productDao.countAllProduct())
                .thenReturn(21);

        //when
        final PagingProductResponse response = productService.getAllProducts(pageRequest);

        //then
        assertSoftly(softly -> {
            softly.assertThat(response.getPageInfo()).usingRecursiveComparison()
                    .isEqualTo(new PageInfo(1, 10, 21, 3));
            softly.assertThat(response.getProducts()).usingRecursiveComparison()
                    .isEqualTo(List.of(
                            ProductResponse.of(new Product(1L, "1", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(2L, "2", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(3L, "3", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(4L, "4", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(5L, "5", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(6L, "6", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(7L, "7", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(8L, "8", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(9L, "9", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(10L, "10", 1000, "image.jpeg"))
                    ));
        });
    }
}
