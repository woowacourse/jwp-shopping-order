package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.MemberEntity;
import cart.domain.OrderEntity;
import cart.domain.OrderItemEntity;
import cart.domain.Product;
import cart.util.CurrentTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderItemDaoTest extends DaoTest {

    private static final MemberEntity memberEntity = new MemberEntity(1L, "email", "password", 0);
    private static final Product product = new Product(1L, "name", 1000, "image", 10);
    private static final OrderEntity orderEntity = new OrderEntity(1L, CurrentTimeUtil.asString(), memberEntity.getId(), 1000, 3000, 0, 4000);

    private MemberDao memberDao;
    private ProductDao productDao;
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
        this.orderDao = new OrderDao(jdbcTemplate);

        setData();
    }

    private void setData() {
        memberDao.insert(memberEntity);
        productDao.insert(product);
        orderDao.insert(orderEntity);
    }

    @Test
    void 데이터를_삽입한다() {
        // given
        OrderItemEntity orderItemEntity = new OrderItemEntity(product.getId(), orderEntity.getId(), product.getName(), product.getPrice(), product.getImageUrl(), 1);

        // when
        long savedId = orderItemDao.insert(orderItemEntity);

        // then
        assertThat(savedId).isNotEqualTo(0);
    }

    @Test
    void ID로_데이터를_조회한다() {
        // given
        OrderItemEntity orderItemEntity = new OrderItemEntity(product.getId(), orderEntity.getId(), product.getName(), product.getPrice(), product.getImageUrl(), 1);
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
        OrderItemEntity orderItemEntity = new OrderItemEntity(product.getId(), orderEntity.getId(), product.getName(), product.getPrice(), product.getImageUrl(), 1);
        orderItemDao.insert(orderItemEntity);

        // when
        OrderItemEntity result = orderItemDao.findByProductId(product.getId());

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getProductName()).isEqualTo(product.getName());
        assertThat(result.getProductPrice()).isEqualTo(product.getPrice());
        assertThat(result.getQuantity()).isEqualTo(1);
    }

    @Test
    void 주문ID로_데이터를_조회한다() {

    }
}
