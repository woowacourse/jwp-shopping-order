package cart.dao;

import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.JdbcTemplateFixture.insertOrder;
import static cart.fixture.JdbcTemplateFixture.insertOrderItem;
import static cart.fixture.JdbcTemplateFixture.insertProduct;
import static cart.fixture.MemberFixture.MEMBER;
import static cart.fixture.OrderItemFixture.getOrderItemEntity;
import static cart.fixture.ProductFixture.CHICKEN;
import static cart.fixture.ProductFixture.PIZZA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderItemDao 은(는)")
@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
    }


    @Test
    void 주문_상품을_저장한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        insertOrder(new OrderEntity(1L, MEMBER.getId(), 20000), jdbcTemplate);

        OrderItemEntity orderItemEntity = new OrderItemEntity(1L, PIZZA.getId(), 10, PIZZA.getName(), PIZZA.getPrice(),
                PIZZA.getImageUrl(), 100000);
        // when
        Long actual = orderItemDao.save(orderItemEntity);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 주문_아이디를_통해_해당하는_주문_상품을_모두_찾는다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);

        long orderId = 1L;
        insertOrder(new OrderEntity(orderId, MEMBER.getId(), 20000), jdbcTemplate);
        OrderItemEntity firstItem = getOrderItemEntity(1L, orderId, 10, PIZZA);
        OrderItemEntity secondItem = getOrderItemEntity(2L, orderId, 5, CHICKEN);
        insertOrderItem(firstItem, jdbcTemplate);
        insertOrderItem(secondItem, jdbcTemplate);

        // when
        List<OrderItemEntity> actual = orderItemDao.findAllByOrderId(orderId);

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual.get(0)).usingRecursiveComparison()
                        .isEqualTo(firstItem),
                () -> assertThat(actual.get(1)).usingRecursiveComparison()
                        .isEqualTo(secondItem)
        );
    }

    @Test
    void 회원_아이디를_통해_해당하는_주문_상품을_모두_찾는다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);

        insertOrder(new OrderEntity(1L, MEMBER.getId(), 20000), jdbcTemplate);
        insertOrder(new OrderEntity(2L, MEMBER.getId(), 10000), jdbcTemplate);
        OrderItemEntity firstItem = getOrderItemEntity(1L, 1L, 10, PIZZA);
        OrderItemEntity secondItem = getOrderItemEntity(2L, 2L, 5, CHICKEN);
        insertOrderItem(firstItem, jdbcTemplate);
        insertOrderItem(secondItem, jdbcTemplate);

        // when
        List<OrderItemEntity> actual = orderItemDao.findAllByMemberId(MEMBER.getId());

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual.get(0)).usingRecursiveComparison()
                        .isEqualTo(firstItem),
                () -> assertThat(actual.get(1)).usingRecursiveComparison()
                        .isEqualTo(secondItem)
        );
    }
}
