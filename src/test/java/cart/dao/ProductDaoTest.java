package cart.dao;

import static fixture.ProductFixture.PRODUCT_3;
import static org.assertj.core.api.Assertions.assertThat;

import anotation.RepositoryTest;
import cart.domain.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("Product 목록 페이지네이션을 테스트한다.")
    void getAllProductPagination() {
        List<Product> secondPageWhenPageSizeTwo = productDao.getAllProductsPagination(2L, 1L);

        assertThat(secondPageWhenPageSizeTwo)
                .usingRecursiveComparison()
                .isEqualTo(List.of(PRODUCT_3));
    }

}