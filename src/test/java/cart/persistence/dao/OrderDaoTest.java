package cart.persistence.dao;

import static cart.fixture.MemberCouponFixture.유효한_멤버쿠폰_엔티티;
import static org.assertj.core.api.Assertions.assertThat;

import cart.fixture.CouponFixture.금액_10000원이상_1000원할인;
import cart.fixture.MemberFixture;
import cart.persistence.dto.OrderDetailDTO;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderEntity;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    private CouponDao couponDao;

    private MemberCouponDao memberCouponDao;

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        couponDao = new CouponDao(jdbcTemplate);
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문을_저장_및_조회한다() {
        // given
        MemberEntity member = MemberFixture.멤버_test1_엔티티;
        long memberId = memberDao.create(member);
        long couponId = couponDao.create(금액_10000원이상_1000원할인.ENTITY);
        MemberCouponEntity memberCoupon = 유효한_멤버쿠폰_엔티티(memberId, couponId);
        long memberCouponId = memberCouponDao.create(memberCoupon);
        OrderEntity order = new OrderEntity(memberId, memberCouponId, 1000, 10000);

        // when
        long id = orderDao.create(order);
        Optional<OrderDetailDTO> result = orderDao.findById(id);

        // then
        Assertions.assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getMemberEntity())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(member),
                () -> assertThat(result.get().getOrderEntity())
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt")
                        .isEqualTo(order),
                () -> assertThat(result.get().getMemberCouponEntity())
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt")
                        .isEqualTo(memberCoupon)
        );
    }
}
