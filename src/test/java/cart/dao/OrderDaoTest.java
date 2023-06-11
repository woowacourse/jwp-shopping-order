package cart.dao;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.repository.OrderEntity;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate, dataSource);
    }

    @Test
    void 주문을_추가한다() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L, LocalDateTime.now(), 0, "결제완료");

        // when
        Long orderId = orderDao.insert(orderEntity);

        // then
        assertThat(orderId).isNotNull();
    }
}
