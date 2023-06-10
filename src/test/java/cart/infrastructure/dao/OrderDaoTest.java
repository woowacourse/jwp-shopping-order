package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.OrderEntity;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;
    private Long memberId;
    private Long couponId;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        final CouponDao couponDao = new CouponDao(jdbcTemplate);
        memberId = memberDao.findById(1L).orElseThrow().getId();
        couponId = couponDao.findById(1L).orElseThrow().getId();
    }

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void testCreate() {
        // given
        final OrderEntity orderEntity = new OrderEntity(null, memberId, couponId, 2_000, 1_000, 1_000, "서울특별시 송파구");

        // when
        final Long id = orderDao.create(orderEntity, memberId);

        // then
        final OrderEntity result = orderDao.findById(id).orElseThrow(NoSuchElementException::new);
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderEntity);
    }

    @Test
    @DisplayName("memberId로 모든 주문 목록을 조회할 수 있다.")
    void testFindAllByMemberId() {
        // given
        final OrderEntity orderEntity1 = new OrderEntity(null, memberId, couponId, 2_000, 1_000, 1_000, "서울특별시 송파구");
        final OrderEntity orderEntity2 = new OrderEntity(null, memberId, couponId, 10_000, 1_000, 2_000, "서울특별시 송파구");
        orderDao.create(orderEntity1, memberId);
        orderDao.create(orderEntity2, memberId);

        // when
        final List<OrderEntity> result = orderDao.findAllByMemberId(memberId);

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(orderEntity1, orderEntity2))
        );
    }
}
