package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Product;
import cart.fixture.MemberFixture;
import cart.fixture.ProductFixture.치킨_15000원;
import cart.fixture.ProductFixture.피자_20000원;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderItemEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 주문_아이템을_저장_및_조회한다() {
        // given
        long memberId = memberDao.create(MemberFixture.멤버_test1_엔티티);
        long orderId = orderDao.create(new OrderEntity(memberId, null, 1000, 10000));
        Product product1 = 피자_20000원.PRODUCT;
        Product product2 = 치킨_15000원.PRODUCT;
        Long productId1 = productDao.create(피자_20000원.ENTITY);
        Long productId2 = productDao.create(치킨_15000원.ENTITY);
        OrderItemEntity orderItemEntity1 = new OrderItemEntity(orderId, productId1, product1.getName(),
                product1.getPrice(), product1.getImageUrl(), 1);
        OrderItemEntity orderItemEntity2 = new OrderItemEntity(orderId, productId2, product2.getName(),
                product2.getPrice(), product2.getImageUrl(), 1);
        List<OrderItemEntity> orderItemEntities = List.of(orderItemEntity1, orderItemEntity2);

        // when
        orderItemDao.createAll(orderItemEntities);
        List<OrderItemEntity> result = orderItemDao.findByOrderId(orderId);

        // then
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderItemEntities);
    }
}
