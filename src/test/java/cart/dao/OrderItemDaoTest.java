package cart.dao;

import cart.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DisplayName("OrderItem Dao 테스트")
@Sql("/data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderItemDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        orderItemDao = new OrderItemDao(dataSource);
    }

    @Test
    @DisplayName("저장 성공")
    void insert_success() {
        // given
        final OrderItemEntity orderItemEntity = new OrderItemEntity(1L, 1L, 1);

        // when
        final OrderItemEntity insertedOrderItemEntity = orderItemDao.insert(orderItemEntity);

        // then
        assertAll(
                () -> assertThat(orderItemEntity.getOrderId()).isEqualTo(insertedOrderItemEntity.getOrderId()),
                () -> assertThat(orderItemEntity.getProductId()).isEqualTo(insertedOrderItemEntity.getProductId()),
                () -> assertThat(orderItemEntity.getQuantity()).isEqualTo(insertedOrderItemEntity.getQuantity())
        );
    }
}
