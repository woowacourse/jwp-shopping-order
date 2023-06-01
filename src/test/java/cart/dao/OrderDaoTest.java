package cart.dao;

import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.ValueFixture.DEFAULT_SIZE;
import static cart.fixture.ValueFixture.LAST_ID_OF_FIRST_PAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderDaoTest {

    Member member;
    OrderDao orderDao;
    OrderEntity orderEntity;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        MemberDao memberDao = new MemberDao(jdbcTemplate);
        member = memberDao.findByEmail(MEMBER_A.getEmail()).get();
        orderDao = new OrderDao(jdbcTemplate);
        orderEntity = new OrderEntity(member.getId(), 1_000, 1_000, 10_000,
                9_000, LocalDateTime.now());
    }

    @Test
    @DisplayName("save는 OrderEntity를 전달하면 DB에 저장하고 ID를 반환한다.")
    void saveSuccessTest() {
        Long actual = orderDao.save(orderEntity);

        assertThat(actual).isPositive();
    }

    @Nested
    @DisplayName("주문 조회 테스트")
    class FindOrderTest {

        Long orderId;

        @BeforeEach
        void setUp() {
            orderId = orderDao.save(orderEntity);
        }

        @Test
        @DisplayName("findByIdAndMemberId는 DB에 존재하는 orderId를 전달하면 해당 ID에 맞는 OrderEntity를 반환한다.")
        void findByIdSuccessTestWithExistsOrderId() {
            Optional<OrderEntity> actual = orderDao.findByOrderId(orderId);
            int expectedEarnedPoints = 1_000;
            int expectedUsedPoints = 1_000;
            int expectedTotalPrice = 10_000;
            int expectedPayPrice = 9_000;

            assertAll(
                    () -> assertThat(actual).isPresent(),
                    () -> assertThat(actual.get().getMemberId()).isEqualTo(member.getId()),
                    () -> assertThat(actual.get().getEarnedPoints()).isEqualTo(expectedEarnedPoints),
                    () -> assertThat(actual.get().getUsedPoints()).isEqualTo(expectedUsedPoints),
                    () -> assertThat(actual.get().getTotalPrice()).isEqualTo(expectedTotalPrice),
                    () -> assertThat(actual.get().getPayPrice()).isEqualTo(expectedPayPrice),
                    () -> assertThat(actual.get().getOrderDate()).isNotNull()
            );
        }

        @Test
        @DisplayName("findByIdAndMemberId는 DB에 존재하지 않는 orderId를 전달하면 빈 Optional을 반환한다.")
        void findByIdSuccessTestWithNotExistsOrderId() {
            Optional<OrderEntity> actual = orderDao.findByOrderId(-999L);

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("findByMemberIdAndLastOrderId은 memberId와 lastOrderId를 전달하면 OrderEntity List를 반환한다.")
        void findAllSuccessTest() {
            List<OrderEntity> actual = orderDao.findByMemberIdAndLastOrderIdAndSize(member.getId(),
                    LAST_ID_OF_FIRST_PAGE, DEFAULT_SIZE);

            assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
        }
    }
}
