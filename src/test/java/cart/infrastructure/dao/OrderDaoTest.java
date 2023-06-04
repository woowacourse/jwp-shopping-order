package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.infrastructure.entity.CouponEntity;
import cart.infrastructure.entity.MemberEntity;
import cart.infrastructure.entity.OrderEntity;
import java.math.BigDecimal;
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
    private Long couponId;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);

        CouponDao couponDao = new CouponDao(jdbcTemplate);
        couponId = couponDao.save(new CouponEntity("쿠폰", "RATE", BigDecimal.valueOf(10), BigDecimal.ZERO));

        MemberDao memberDao = new MemberDao(jdbcTemplate);
        memberId = memberDao.save(new MemberEntity("email@email.com", "password"));
    }

    @Test
    void 주문을_저장한다() {
        // given
        OrderEntity orderEntity = new OrderEntity(memberId, couponId, 3000, "2020060102301", LocalDateTime.now());

        // when
        Long savedId = orderDao.save(orderEntity);

        // then
        assertThat(savedId).isPositive();
    }

    @Test
    void 쿠폰이_없어도_주문을_저장할_수_있다() {
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
    void 쿠폰을_사용하지_않은_주문을_id로_조회할_때_쿠폰_id가_0으로_반환한다() {
        // given
        OrderEntity orderEntity = new OrderEntity(memberId, 3000, "2020060102301", LocalDateTime.now());
        Long saveId = orderDao.save(orderEntity);

        // when
        Optional<OrderEntity> findOrderEntity = orderDao.findById(saveId);

        // then
        assertThat(findOrderEntity.get().getCouponId()).isEqualTo(0);
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
