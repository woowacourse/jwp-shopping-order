package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.order.OrderWithMemberDto;
import cart.dao.dto.order.OrderDto;
import cart.domain.Member;
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

    private final Member member = new Member(1L, "a@a.com", "1234");
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
        OrderDto orderDto = new OrderDto(1L);

        // when
        long savedId = orderDao.save(orderDto);

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @Test
    @DisplayName("저장된 주문 정보를 조회할 수 있다.")
    void findById() {
        // given
        OrderDto orderDto = new OrderDto(1L);
        long savedId = orderDao.save(orderDto);

        // when
        Optional<OrderWithMemberDto> foundOrder = orderDao.findById(savedId);

        // then
        OrderWithMemberDto expectedResult = new OrderWithMemberDto(savedId, 1L,
            Timestamp.valueOf(LocalDateTime.now()),
            member.getEmail(), member.getPassword());

        assertThat(foundOrder).isPresent()
            .get()
            .usingRecursiveComparison()
            .ignoringFields("createdAt")
            .isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("사용자의 id로 모든 주문 정보를 조회할 수 있다.")
    void findAllByMemberId() {
        // given
        long savedId1 = orderDao.save(new OrderDto(1L));
        long savedId2 = orderDao.save(new OrderDto(1L));
        long savedId3 = orderDao.save(new OrderDto(2L));

        // when
        List<OrderWithMemberDto> ordersWithMember = orderDao.findAllByMemberId(1L);

        // then
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        assertThat(ordersWithMember).usingRecursiveComparison()
            .ignoringFields("createdAt")
            .isEqualTo(List.of(
                new OrderWithMemberDto(savedId1, 1L, time, member.getEmail(), member.getPassword()),
                new OrderWithMemberDto(savedId2, 1L, time, member.getEmail(), member.getPassword())
            ));
    }
}