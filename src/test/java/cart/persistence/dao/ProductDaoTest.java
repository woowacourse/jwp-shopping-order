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
                ProductEntity::getImageUrl, ProductEntity::isDeleted)
            .containsExactly(저장된_치킨_아이디, "치킨", 20000, "chicken_image_url", false);
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
        // given
        final ProductEntity 치킨 = new ProductEntity("치킨", "chicken_image_url", 20000, false);

        // when
        final Long 저장된_치킨_아이디 = productDao.insert(치킨);

        // then
        final Optional<ProductEntity> product = productDao.getProductById(저장된_치킨_아이디);
        final ProductEntity findProduct = product.get();
        assertThat(findProduct)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl, ProductEntity::isDeleted)
            .containsExactly(저장된_치킨_아이디, "치킨", 20000, "chicken_image_url", false);
    }

    @DisplayName("제거되지 않은 상품 정보 전체를 조회한다.")
    @Test
    void getNotDeletedProducts() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_피자_아이디 = 피자_저장();
        productDao.updateProductDeleted(저장된_피자_아이디);

        // when
        final List<ProductEntity> products = productDao.getNotDeletedProducts();

        // then
        assertThat(products).hasSize(1);
        assertThat(products)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl, ProductEntity::isDeleted)
            .containsExactly(
                tuple(저장된_치킨_아이디, "치킨", 20000, "chicken_image_url", false));
    }

    @DisplayName("주어진 상품 아이디에 해당하는 상품 정보를 수정한다.")
    @Test
    void updateProduct() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();

        // when
        final ProductEntity 수정_요청_엔티티 = new ProductEntity(저장된_치킨_아이디, "탕수육", "pork_image_url", 30000, false);
        int updatedCount = productDao.updateProduct(저장된_치킨_아이디, 수정_요청_엔티티);

        // then
        final Optional<ProductEntity> product = productDao.getProductById(저장된_치킨_아이디);
        final ProductEntity findProduct = product.get();

        assertThat(updatedCount).isEqualTo(1);
        assertThat(findProduct)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl, ProductEntity::isDeleted)
            .containsExactly(저장된_치킨_아이디, "탕수육", 30000, "pork_image_url", false);
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

    @Test
    @DisplayName("상품의 제거 상태를 업데이트한다.")
    void updateDeleteStatus() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();

        // when
        productDao.updateProductDeleted(저장된_치킨_아이디);

        // then
        final Optional<ProductEntity> product = productDao.getProductById(저장된_치킨_아이디);
        assertThat(product)
            .isEmpty();
    }

    @Test
    @DisplayName("상품 리스트를 페이지 단위로 반환한다.")
    void getProductsByPage() {
        // given
        final ProductEntity 치킨 = new ProductEntity("치킨", "chicken_image_url", 20000, false);
        final Long 저장된_치킨_아이디 = productDao.insert(치킨);

        final ProductEntity 치킨2 = new ProductEntity("치킨2", "chicken_image_url", 20000, false);
        final Long 저장된_치킨_아이디2 = productDao.insert(치킨2);

        final ProductEntity 치킨3 = new ProductEntity("치킨3", "chicken_image_url", 20000, false);
        final Long 저장된_치킨_아이디3 = productDao.insert(치킨3);

        final ProductEntity 치킨4 = new ProductEntity("치킨4", "chicken_image_url", 20000, false);
        final Long 저장된_치킨_아이디4 = productDao.insert(치킨4);

        final ProductEntity 치킨5 = new ProductEntity("치킨5", "chicken_image_url", 20000, false);
        final Long 저장된_치킨_아이디5 = productDao.insert(치킨5);

        // when
        final List<ProductEntity> 첫번째_페이지 = productDao.getProductsByPage(1, 3);
        final List<ProductEntity> 두번째_페이지 = productDao.getProductsByPage(2, 3);

        // then
        assertThat(첫번째_페이지).hasSize(3);
        assertThat(첫번째_페이지)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl, ProductEntity::isDeleted)
            .containsExactly(
                tuple(저장된_치킨_아이디, "치킨", 20000, "chicken_image_url", false),
                tuple(저장된_치킨_아이디2, "치킨2", 20000, "chicken_image_url", false),
                tuple(저장된_치킨_아이디3, "치킨3", 20000, "chicken_image_url", false));

        assertThat(두번째_페이지).hasSize(2);
        assertThat(두번째_페이지)
            .extracting(ProductEntity::getId, ProductEntity::getName, ProductEntity::getPrice,
                ProductEntity::getImageUrl, ProductEntity::isDeleted)
            .containsExactly(
                tuple(저장된_치킨_아이디4, "치킨4", 20000, "chicken_image_url", false),
                tuple(저장된_치킨_아이디5, "치킨5", 20000, "chicken_image_url", false));
    }

    @Test
    @DisplayName("전체 상품의 개수를 구한다.")
    void getAllProductCount() {
        // given
        final ProductEntity 치킨 = new ProductEntity("치킨", "chicken_image_url", 20000, false);
        productDao.insert(치킨);

        final ProductEntity 치킨2 = new ProductEntity("치킨2", "chicken_image_url", 20000, false);
        productDao.insert(치킨2);

        final ProductEntity 치킨3 = new ProductEntity("치킨3", "chicken_image_url", 20000, false);
        productDao.insert(치킨3);

        final ProductEntity 치킨4 = new ProductEntity("치킨4", "chicken_image_url", 20000, false);
        productDao.insert(치킨4);

        final ProductEntity 치킨5 = new ProductEntity("치킨5", "chicken_image_url", 20000, false);
        productDao.insert(치킨5);

        // when
        final long productCount = productDao.getAllProductCount();

        // then
        assertThat(productCount)
            .isEqualTo(5);
    }
}
