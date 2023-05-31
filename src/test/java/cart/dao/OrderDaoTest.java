package cart.dao;

import cart.domain.product.Product;
import cart.entity.OrderDetailEntity;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.common.fixture.DomainFixture.EMAIL;
import static cart.common.fixture.DomainFixture.PASSWORD;
import static cart.common.fixture.DomainFixture.PRODUCT_IMAGE;
import static cart.common.fixture.DomainFixture.PRODUCT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test.sql")
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;
    private Long memberId;
    private Long productId;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);

        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        final ProductDao productDao = new ProductDao(jdbcTemplate);

        memberId = memberDao.addMember(new MemberEntity(EMAIL, PASSWORD, 1000));
        productId = productDao.createProduct(new Product(PRODUCT_NAME, 20000, PRODUCT_IMAGE));
    }

    @Test
    void 주문_정보를_추가한다() {
        //given
        final OrderEntity orderEntity = new OrderEntity(memberId, 19000, 1000);

        //when
        final Long id = orderDao.addOrder(orderEntity);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 주문_상세_정보를_추가한다() {
        //given
        final Long orderId = orderDao.addOrder(new OrderEntity(memberId, 19000, 1000));
        final OrderDetailEntity orderDetailEntity = new OrderDetailEntity(orderId, productId, 1);

        //when
        final Long orderDetailId = orderDao.addOrderDetail(orderDetailEntity);

        //then
        assertThat(orderDetailId).isEqualTo(1L);
    }

    @Test
    void id로_주문_정보를_얻는다() {
        //given
        final Long id = orderDao.addOrder(new OrderEntity(memberId, 19000, 1000));

        //when
        final OrderEntity orderEntity = orderDao.getOrderEntityById(id);

        //then
        assertThat(orderEntity).usingRecursiveComparison()
                .isEqualTo(new OrderEntity(1L, 1L, 19000, 1000));
    }

    @Test
    void 모든_주문_정보를_얻는다() {
        //given
        orderDao.addOrder(new OrderEntity(memberId, 19000, 1000));

        //when
        final List<OrderEntity> orderEntities = orderDao.getAllOrders();

        //then
        assertThat(orderEntities).usingRecursiveComparison()
                .isEqualTo(List.of(new OrderEntity(1L, 1L, 19000, 1000)));
    }

    @Test
    void 주문_id로_주문_상세_정보를_얻는다() {
        //given
        final Long orderId = orderDao.addOrder(new OrderEntity(memberId, 19000, 1000));
        orderDao.addOrderDetail(new OrderDetailEntity(orderId, productId, 1));

        //when
        final List<OrderDetailEntity> detailEntities = orderDao.getOrderDetailEntitiesByOrderId(orderId);

        //then
        assertThat(detailEntities).usingRecursiveComparison()
                .isEqualTo(List.of(new OrderDetailEntity(1L, 1L, 1L, 1)));
    }
}
