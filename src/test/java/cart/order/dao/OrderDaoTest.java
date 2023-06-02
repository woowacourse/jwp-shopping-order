package cart.order.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cart.coupon.dao.CouponDao;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.EmptyCoupon;
import cart.coupon.domain.FixDiscountCoupon;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.exception.NotFoundOrderException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class OrderDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private OrderDao orderDao;

  private MemberDao memberDao;
  private CouponDao couponDao;

  @BeforeEach
  void setUp() {
    memberDao = mock(MemberDao.class);
    couponDao = mock(CouponDao.class);

    orderDao = new OrderDao(jdbcTemplate, memberDao, couponDao);
  }

  @Test
  @DisplayName("findByMemberId() : member Id를 통해 사용자가 주문한 주문 목록들을 조회할 수 있다.")
  void test_findByMemberId() throws Exception {
    //given
    final long memberId = 1L;

    final Member member = new Member(1L, "email", "password");
    final Coupon coupon = new EmptyCoupon();

    when(memberDao.getMemberById(anyLong()))
        .thenReturn(member);

    when(couponDao.findById(anyLong()))
        .thenReturn(coupon);

    //when
    final List<Order> orders = orderDao.findByMemberId2(memberId);

    //then
    assertEquals(3, orders.size());
  }

  @Test
  @DisplayName("findByOrderId() : order Id를 통해 주문 목록을 조회할 수 있다.")
  void test_findByOrderId() throws Exception {
    //given
    final long orderId = 1L;

    //when
    final Order order = orderDao.findByOrderId2(orderId);

    //then
    assertEquals(order.getId(), orderId);
  }

  @Test
  @DisplayName("deleteByOrderId() : order Id를 통해 주문을 삭제할 수 있다.")
  void test_deleteByOrderId() throws Exception {
    //given
    final long orderId = 1L;

    //when
    orderDao.deleteByOrderId(1L);

    //then
    assertThatThrownBy(() -> orderDao.findByOrderId2(orderId))
        .isInstanceOf(NotFoundOrderException.class);
  }
}
