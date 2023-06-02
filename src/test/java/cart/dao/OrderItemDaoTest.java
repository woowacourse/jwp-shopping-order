package cart.dao;

import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.JdbcTemplateFixture.insertOrder;
import static cart.fixture.JdbcTemplateFixture.insertProduct;
import static cart.fixture.MemberFixture.MEMBER;
import static cart.fixture.ProductFixture.CHICKEN;
import static cart.fixture.ProductFixture.PIZZA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
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
    void 전달받은_아이템을_모두_저장한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        OrderEntity orderEntity = new OrderEntity(1L, MEMBER.getId(), 10000);
        insertOrder(orderEntity, jdbcTemplate);
        insertProduct(CHICKEN,jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        OrderItemEntity chicken = getOrderItemEntity(orderEntity.getId(), CHICKEN, 5);
        OrderItemEntity pizza = getOrderItemEntity(orderEntity.getId(), PIZZA, 10);
        List<OrderItemEntity> orderItemEntities = List.of(chicken, pizza);

        // when & then
        assertThatNoException().isThrownBy(() -> orderItemDao.batchSave(orderItemEntities));
    }

    @Test
    void 주문_아이디를_통해_아이템을_찾는다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        OrderEntity orderEntity = new OrderEntity(1L, MEMBER.getId(), 10000);
        insertOrder(orderEntity, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        OrderItemEntity orderChicken = getOrderItemEntity(orderEntity.getId(), CHICKEN, 5);
        OrderItemEntity orderPizza = getOrderItemEntity(orderEntity.getId(), PIZZA, 10);
        List<OrderItemEntity> orderItemEntities = List.of(orderChicken, orderPizza);
        orderItemDao.batchSave(orderItemEntities);

        // when
        List<OrderItemEntity> actual = orderItemDao.findAllByOrderId(orderEntity.getId());

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual.get(0)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(orderChicken),
                () -> assertThat(actual.get(1)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(orderPizza)
        );
    }

    private OrderItemEntity getOrderItemEntity(Long orderId, Product product, int quantity) {
        return new OrderItemEntity(orderId, product.getId(), quantity, product.getName(), product.getPrice(), product.getImageUrl(), 1000);
    }
}
