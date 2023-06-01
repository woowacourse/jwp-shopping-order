package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    private Long memberId;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);

        MemberDao memberDao = new MemberDao(jdbcTemplate);
        memberId = memberDao.save(new MemberEntity("email@email.com", "password"));
    }

    @Test
    void 주문을_저장한다() {
        // given
        OrderEntity orderEntity = new OrderEntity(memberId, 3000, "2020060102301", LocalDateTime.now());

        // when
        Long savedId = orderDao.save(orderEntity);

        // then
        assertThat(savedId).isPositive();
    }

    @Test
    void 주문을_사용자_id로_조회한다() {
        // given
        OrderEntity orderEntity = new OrderEntity(memberId, 3000, "2020060102301", LocalDateTime.now());
        orderDao.save(orderEntity);

        // when
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);

        // then
        assertThat(orderEntities).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(orderEntity));
    }

    @Test
    void 주문을_id로_조회한다() {
        // given
        OrderEntity orderEntity = new OrderEntity(memberId, 3000, "2020060102301", LocalDateTime.now());
        Long id = orderDao.save(orderEntity);

        // when
        Optional<OrderEntity> findOrderEntity = orderDao.findById(id);

        // then
        assertThat(findOrderEntity).isPresent();
        assertThat(findOrderEntity.get()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(orderEntity);
    }
}
