package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.MemberEntity;
import cart.entity.OrderItemEntity;
import cart.entity.OrdersEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderItemDao orderItemDao;

    private OrdersDao ordersDao;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
        ordersDao = new OrdersDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 주문상품을_저장한다() {
        // given
        final MemberEntity memberEntity = memberDao.insert(new MemberEntity("우가@n.com", "1234"));
        final OrdersEntity ordersEntity = ordersDao.insert(new OrdersEntity(1000L, null, memberEntity.getId()));
        final OrderItemEntity orderItemEntity = new OrderItemEntity(
                "상품",
                1000,
                "상품.jpg",
                10,
                ordersEntity.getId()
        );

        // when
        OrderItemEntity savedOrderItemEntity = orderItemDao.insert(orderItemEntity);

        // then
        assertThat(savedOrderItemEntity.getName()).isEqualTo(orderItemEntity.getName());
        assertThat(savedOrderItemEntity.getPrice()).isEqualTo(orderItemEntity.getPrice());
        assertThat(savedOrderItemEntity.getImageUrl()).isEqualTo(orderItemEntity.getImageUrl());
    }

    @Test
    void 주문에_해당하는_주문상품_전체를_조회한다() {
        // given
        final MemberEntity memberEntity = memberDao.insert(new MemberEntity("우가@n.com", "1234"));
        final OrdersEntity ordersEntity = ordersDao.insert(new OrdersEntity(1000L, null, memberEntity.getId()));
        final OrderItemEntity orderItemEntity1 = new OrderItemEntity(
                "상품",
                1000,
                "상품.jpg",
                10,
                ordersEntity.getId()
        );

        final OrderItemEntity orderItemEntity2 = new OrderItemEntity(
                "상품2",
                2000,
                "상품2.jpg",
                20,
                ordersEntity.getId()
        );

        orderItemDao.insert(orderItemEntity1);
        orderItemDao.insert(orderItemEntity2);

        // when
        List<OrderItemEntity> result = orderItemDao.findAllByOrderId(ordersEntity.getId());

        // then
        assertThat(result).hasSize(2);
        assertThat(result).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(orderItemEntity1, orderItemEntity2));
    }
}
