package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ProductDao.class, MemberDao.class, CartItemDao.class})
class ProductDaoTest extends DaoTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CartItemDao cartItemDao;

    private ProductEntity 치킨, 피자;

    @BeforeEach
    void setUp() {
        치킨 = new ProductEntity("치킨", "chicken_image_url", 20000);
        피자 = new ProductEntity("피자", "pizza_image_url", 30000);
    }

    @DisplayName("유효한 상품 아이디가 주어지면, 상품 정보를 조회한다")
    @Test
    void getProductById_success() {
        // given
        final Long 저장된_치킨_아이디 = productDao.insert(치킨);

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
        final Long 저장된_치킨_아이디 = productDao.insert(치킨);

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
        final Long 저장된_치킨_아이디 = productDao.insert(치킨);
        final Long 저장된_피자_아이디 = productDao.insert(피자);

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
        final Long 저장된_치킨_아이디 = productDao.insert(치킨);

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
        final Long productId = productDao.insert(치킨);

        // when
        int deletedCount = productDao.deleteProduct(productId);

        // then
        final Optional<ProductEntity> product = productDao.getProductById(productId);

        assertThat(product).isEmpty();
        assertThat(deletedCount).isEqualTo(1);
    }
}
