package cart.order.dao;

import cart.config.DaoTest;
import cart.order.domain.OrderHistory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixtures.MemberFixtures.Member_Dooly;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class OrderHistoryDaoTest extends DaoTest {

    @Test
    void 특정_멤버의_장바구니_주문을_조회한다() {
        // given
        final List<OrderHistory> orderHistories = orderHistoryDao.findByMemberId(Member_Dooly.ID);

        // when, then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderHistories).hasSize(2);
            softAssertions.assertThat(orderHistories.get(0).getId()).isEqualTo(4L);
            softAssertions.assertThat(orderHistories.get(0).getMember()).usingRecursiveComparison().isEqualTo(Member_Dooly.ENTITY);
            softAssertions.assertThat(orderHistories.get(0).getTotalPrice()).isEqualTo(40000);
            softAssertions.assertThat(orderHistories.get(1).getId()).isEqualTo(1L);
            softAssertions.assertThat(orderHistories.get(1).getMember()).usingRecursiveComparison().isEqualTo(Member_Dooly.ENTITY);
            softAssertions.assertThat(orderHistories.get(1).getTotalPrice()).isEqualTo(20000);
        });
    }

    @Test
    void 장바구니_ID를_통해_장바구니를_조회하다() {
        // given
        final OrderHistory orderHistory = orderHistoryDao.findById(1L);

        // when, then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderHistory.getId()).isEqualTo(1L);
            softAssertions.assertThat(orderHistory.getMember()).usingRecursiveComparison().isEqualTo(Member_Dooly.ENTITY);
            softAssertions.assertThat(orderHistory.getTotalPrice()).isEqualTo(20000);
        });
    }

    @Test
    void 장바구니를_저장하다() {
        // given
        final OrderHistory orderHistory = new OrderHistory(Member_Dooly.ENTITY, 50000L);

        // when
        final Long orderHistoryId = orderHistoryDao.save(orderHistory);

        // then
        assertThat(orderHistoryId).isEqualTo(7L);
    }
}
