package cart.order.dao;

import cart.config.DaoTest;
import cart.order.domain.CartOrder;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixtures.MemberFixtures.*;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class CartOrderDaoTest extends DaoTest {

    @Test
    void 특정_멤버의_장바구니_주문을_조회한다() {
        // given
        final List<CartOrder> cartOrders = cartOrderDao.findByMemberId(Member_Dooly.ID);

        // when, then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartOrders).hasSize(2);
            softAssertions.assertThat(cartOrders.get(0).getId()).isEqualTo(4L);
            softAssertions.assertThat(cartOrders.get(0).getMember()).isEqualTo(Member_Dooly.ENTITY);
            softAssertions.assertThat(cartOrders.get(0).getTotalPrice()).isEqualTo(40000);
            softAssertions.assertThat(cartOrders.get(1).getId()).isEqualTo(1L);
            softAssertions.assertThat(cartOrders.get(1).getMember()).isEqualTo(Member_Dooly.ENTITY);
            softAssertions.assertThat(cartOrders.get(1).getTotalPrice()).isEqualTo(20000);
        });
    }

    @Test
    void 장바구니_ID를_통해_장바구니를_조회하다() {
        // given
        final CartOrder cartOrder = cartOrderDao.findById(1L);

        // when, then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartOrder.getId()).isEqualTo(1L);
            softAssertions.assertThat(cartOrder.getMember()).isEqualTo(Member_Dooly.ENTITY);
            softAssertions.assertThat(cartOrder.getTotalPrice()).isEqualTo(20000);
        });
    }

    @Test
    void 장바구니를_저장하다() {
        // given
        final CartOrder cartOrder = new CartOrder(Member_Dooly.ENTITY, 50000L);

        // when
        final Long cartOrderId = cartOrderDao.save(cartOrder);

        // then
        assertThat(cartOrderId).isEqualTo(7L);
    }
}
