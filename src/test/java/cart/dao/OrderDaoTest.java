package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.OrderDto;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@JdbcTest
class OrderDaoTest {

    private static final RowMapper<OrderDto> orderDtoRowMapper = (rs, rn) -> new OrderDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("coupon_id"),
            rs.getTimestamp("time_stamp").toLocalDateTime()
    );

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;

    @BeforeEach
    void beforeEach() {
        orderDao = new OrderDao(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
    }

    @Test
    @DisplayName("orderDto를 저장하는 기능 테스트")
    void insertTest() {
        final LocalDateTime dateTime = LocalDateTime.now().withNano(0);
        final OrderDto orderDto = new OrderDto(1L, 2L, dateTime);

        final Long orderId = orderDao.insert(orderDto);

        final Optional<OrderDto> order = orderDao.findById(orderId);
        assertThat(order.get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderDto);
    }

    @Test
    @DisplayName("orderDto를 조회하는 기능 테스트")
    void findByIdTest() {
        final LocalDateTime dateTime = LocalDateTime.now().withNano(0);
        final OrderDto orderDto = new OrderDto(1L, 2L, dateTime);
        final Long orderId = orderDao.insert(orderDto);

        final OrderDto result = orderDao.findById(orderId)
                .orElseThrow(RuntimeException::new);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderDto);
    }
}
