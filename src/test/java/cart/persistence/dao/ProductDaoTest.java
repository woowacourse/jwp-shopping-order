package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ProductDao.class})
class ProductDaoTest extends DaoTestHelper {

    @Autowired
    private ProductDao productDao;


    @DisplayName("유효한 상품 아이디가 주어지면, 상품 정보를 조회한다")
    @Test
    void getProductById_success() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();

        // when
        final Optional<ProductEntity> product = productDao.getProductById(저장된_치킨_아이디);

        // then
        final ProductEntity findProduct = product.get();
        assertThat(findProduct)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl)
            .containsExactly(저장된_치킨_아이디, "치킨", 20000, "chicken_image_url");
    }

    @DisplayName("유효하지 않은 상품 아이디가 주어지면, 빈 값을 반환한다")
    @Test
    void getProductById_empty() {
        // when
        final Optional<ProductEntity> findProduct = productDao.getProductById(1L);

        // then
        assertThat(findProduct).isEmpty();
    }

    @DisplayName("상품 정보를 저장한다")
    @Test
    void insert() {
        // when
        final Long 저장된_치킨_아이디 = 치킨_저장();

        // then
        final Optional<ProductEntity> product = productDao.getProductById(저장된_치킨_아이디);
        final ProductEntity findProduct = product.get();
        assertThat(findProduct)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl)
            .containsExactly(저장된_치킨_아이디, "치킨", 20000, "chicken_image_url");
    }

    @DisplayName("상품 정보 전체를 조회한다.")
    @Test
    void getAllProducts() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_피자_아이디 = 피자_저장();

        // when
        final List<ProductEntity> products = productDao.getAllProducts();

        // then
        assertThat(products).hasSize(2);
        assertThat(products)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl)
            .containsExactly(
                tuple(저장된_치킨_아이디, "치킨", 20000, "chicken_image_url"),
                tuple(저장된_피자_아이디, "피자", 30000, "pizza_image_url"));
    }

    @DisplayName("주어진 상품 아이디에 해당하는 상품 정보를 수정한다.")
    @Test
    void updateProduct() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();

        // when
        final ProductEntity 수정_요청_엔티티 = new ProductEntity(저장된_치킨_아이디, "탕수육", "pork_image_url", 30000);
        int updatedCount = productDao.updateProduct(저장된_치킨_아이디, 수정_요청_엔티티);

        // then
        final Optional<ProductEntity> product = productDao.getProductById(저장된_치킨_아이디);
        final ProductEntity findProduct = product.get();

        assertThat(updatedCount).isEqualTo(1);
        assertThat(findProduct)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl)
            .containsExactly(저장된_치킨_아이디, "탕수육", 30000, "pork_image_url");
    }

    @DisplayName("주어진 상품 아이디에 해당하는 상품을 삭제한다.")
    @Test
    void deleteProduct() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();

        // when
        int deletedCount = productDao.deleteProduct(저장된_치킨_아이디);

        // then
        final Optional<ProductEntity> product = productDao.getProductById(저장된_치킨_아이디);

        assertThat(product).isEmpty();
        assertThat(deletedCount).isEqualTo(1);
    }

    @Test
    @DisplayName("주어진 상품 아이디에 해당하는 상품이 있으면 true를 반환한다.")
    void existById_true() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();

        // when
        final boolean result = productDao.existById(저장된_치킨_아이디);

        // then
        assertThat(result)
            .isTrue();
    }

    @Test
    @DisplayName("주어진 상품 아이디에 해당하는 상품이 없으면 false를 반환한다.")
    void existById_false() {
        // when
        final boolean result = productDao.existById(1L);

        // then
        assertThat(result)
            .isFalse();
    }
}
