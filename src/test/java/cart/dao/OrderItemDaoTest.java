package cart.dao;

import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderItemDao orderItemDao;

    private OrderDao orderDao;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.orderItemDao = new OrderItemDao(jdbcTemplate.getDataSource());
        this.orderDao = new OrderDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 주문_상품을_저장한다() {
        // given
        final MemberEntity memberEntity = new MemberEntity("pizza1@pizza.com", "pizza");
        final MemberEntity member = memberDao.insert(memberEntity);

        final OrderEntity orderEntity = new OrderEntity(BigDecimal.valueOf(3000L), null, member.getId());
        final OrderEntity order = orderDao.insert(orderEntity);

        final OrderItemEntity orderItemEntity = new OrderItemEntity("치즈피자", "치즈피자.png", BigDecimal.valueOf(8900L), 3, order.getId());

        // when
        orderItemDao.insert(orderItemEntity);

        // then
        final int row = JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_item");
        assertThat(row).isEqualTo(1);
    }

    @Test
    void 주문_상품을_수정한다() {
        // given
        final MemberEntity memberEntity = new MemberEntity("pizza1@pizza.com", "pizza");
        final MemberEntity member = memberDao.insert(memberEntity);

        final OrderEntity orderEntity = new OrderEntity(BigDecimal.valueOf(3000L), null, member.getId());
        final OrderEntity order = orderDao.insert(orderEntity);

        final OrderItemEntity orderItemEntity = new OrderItemEntity("치즈피자", "치즈피자.png", BigDecimal.valueOf(8900L), 3, order.getId());
        final OrderItemEntity orderItem = orderItemDao.insert(orderItemEntity);

        final OrderItemEntity updatedEntity = new OrderItemEntity(orderItem.getId(), "불고기피자", "불고기피자.png", BigDecimal.valueOf(7900L), 2, order.getId());

        // when
        orderItemDao.update(updatedEntity);

        // then
        final OrderItemEntity result = orderItemDao.findByOrderId(order.getId()).get(0);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(updatedEntity.getId()),
                () -> assertThat(result.getName()).isEqualTo(updatedEntity.getName()),
                () -> assertThat(result.getImageUrl()).isEqualTo(updatedEntity.getImageUrl()),
                () -> assertThat(result.getQuantity()).isEqualTo(updatedEntity.getQuantity()),
                () -> assertThat(result.getPrice()).isEqualTo(updatedEntity.getPrice()),
                () -> assertThat(result.getOrderId()).isEqualTo(updatedEntity.getOrderId())
        );
    }

    @Test
    void 주문_번호에_해당하는_주문_상품을_조회한다() {
        // given
        final MemberEntity memberEntity = new MemberEntity("pizza1@pizza.com", "pizza");
        final MemberEntity member = memberDao.insert(memberEntity);

        final OrderEntity orderEntity = new OrderEntity(BigDecimal.valueOf(3000L), null, member.getId());
        final OrderEntity order = orderDao.insert(orderEntity);

        final OrderItemEntity orderItemEntity1 = new OrderItemEntity("치즈피자", "치즈피자.png", BigDecimal.valueOf(8900L), 3, order.getId());
        final OrderItemEntity orderItemEntity2 = new OrderItemEntity("불고기피자", "불고기피자.png", BigDecimal.valueOf(7900L), 1, order.getId());
        final OrderItemEntity orderItemEntity3 = new OrderItemEntity("페페로니피자", "페페로니피자.png", BigDecimal.valueOf(9900L), 2, order.getId());
        orderItemDao.insert(orderItemEntity1);
        orderItemDao.insert(orderItemEntity2);
        orderItemDao.insert(orderItemEntity3);

        // when
        final List<OrderItemEntity> result = orderItemDao.findByOrderId(order.getId());

        // then
        assertThat(result.size()).isEqualTo(3);
    }
}