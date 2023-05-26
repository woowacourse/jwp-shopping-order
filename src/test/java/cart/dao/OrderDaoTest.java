package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.OrderDto;
import java.time.LocalDateTime;
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
        LocalDateTime dateTime = LocalDateTime.now();
        OrderDto orderDto = new OrderDto(2L, dateTime);

        Long orderId = orderDao.insert(orderDto);

        String sql = "SELECT * FROM orders WHERE id = ?";
        OrderDto queryResultOrder = jdbcTemplate.queryForObject(sql, orderDtoRowMapper, orderId);
        assertThat(orderDto.getMemberId()).isEqualTo(queryResultOrder.getMemberId());
        assertThat(orderDto.getTimeStamp()).isEqualTo(queryResultOrder.getTimeStamp());
    }

    @Test
    @DisplayName("orderDto를 조회하는 기능 테스트")
    void findByIdTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        OrderDto orderDto = new OrderDto(1L, 2L, dateTime);
        String sql = "INSERT INTO orders (id, member_id, time_stamp) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, orderDto.getId(), orderDto.getMemberId(), orderDto.getTimeStamp());

        OrderDto queryResultOrder = orderDao.findById(orderDto.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(orderDto.getMemberId()).isEqualTo(queryResultOrder.getMemberId());
        assertThat(orderDto.getTimeStamp()).isEqualTo(queryResultOrder.getTimeStamp());
    }

}