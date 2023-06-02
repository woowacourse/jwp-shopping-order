package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.OrderEntity;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/init_cart_item.sql"})
class OrderDaoTest {

    private OrderDao orderDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문 정보를 저장할 수 있다.")
    void save() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L);

        // when
        long savedId = orderDao.save(orderEntity);

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @Test
    @DisplayName("저장된 주문 정보를 조회할 수 있다.")
    void findById() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L);
        long savedId = orderDao.save(orderEntity);
        OrderEntity savedEntity = new OrderEntity(savedId, 1L);

        // when
        Optional<OrderEntity> foundEntity = orderDao.findById(savedId);

        // then
        assertThat(foundEntity).isPresent()
            .get()
            .usingRecursiveComparison()
            .ignoringFields("createdAt")
            .isEqualTo(savedEntity);
    }

    @Test
    @DisplayName("사용자의 id로 모든 주문 정보를 조회할 수 있다.")
    void findAllByMemberId() {
        // given
        long savedId1 = orderDao.save(new OrderEntity(1L));
        long savedId2 = orderDao.save(new OrderEntity(1L));
        long savedId3 = orderDao.save(new OrderEntity(2L));

        // when
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(1L);

        // then
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        assertThat(orderEntities).usingRecursiveComparison()
            .ignoringFields("createdAt")
            .isEqualTo(List.of(
                new OrderEntity(savedId1, 1L, time),
                new OrderEntity(savedId2, 1L, time)
            ));
    }
}