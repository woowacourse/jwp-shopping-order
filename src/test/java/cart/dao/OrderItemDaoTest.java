package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.OrderItemEntity;
import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderItemDaoTest extends DaoTest {

    private static final Product product = new Product(1L, "name", 1000, "image", 10);

    private ProductDao productDao;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);

        productDao.insert(product);
    }

    @Test
    void 데이터를_삽입한다() {
        // given
        OrderItemEntity orderItemEntity = new OrderItemEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), 1);

        // when
        long savedId = orderItemDao.insert(orderItemEntity);

        // then
        assertThat(savedId).isNotEqualTo(0);
    }

    @Test
    void ID로_데이터를_조회한다() {
        // given
        OrderItemEntity orderItemEntity = new OrderItemEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), 1);
        long savedId = orderItemDao.insert(orderItemEntity);

        // when
        OrderItemEntity result = orderItemDao.findById(savedId);

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getProductName()).isEqualTo(product.getName());
        assertThat(result.getProductPrice()).isEqualTo(product.getPrice());
        assertThat(result.getQuantity()).isEqualTo(1);
    }

    @Test
    void 상품ID로_데이터를_조회한다() {
        // given
        OrderItemEntity orderItemEntity = new OrderItemEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), 1);
        orderItemDao.insert(orderItemEntity);

        // when
        OrderItemEntity result = orderItemDao.findByProductId(product.getId());

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getProductName()).isEqualTo(product.getName());
        assertThat(result.getProductPrice()).isEqualTo(product.getPrice());
        assertThat(result.getQuantity()).isEqualTo(1);
    }
}
