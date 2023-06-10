package cart.order.dao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cart.coupon.dao.CouponDao;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.EmptyCoupon;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderedItems;
import cart.order.exception.OrderException;
import cart.order.exception.OrderExceptionType;
import cart.value_object.Money;
import java.util.List;
import java.util.Optional;
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
  private OrderItemDao orderItemDao;

  @BeforeEach
  void setUp() {
    memberDao = mock(MemberDao.class);
    couponDao = mock(CouponDao.class);
    orderItemDao = mock(OrderItemDao.class);

    orderDao = new OrderDao(jdbcTemplate, memberDao, couponDao, orderItemDao);
  }

  @Test
  @DisplayName("save() : 주문 시 사용하는 쿠폰이 없어도 제대로 주문이 저장될 수 있다.")
  void test_save() throws Exception {
    //given
    final Member member = new Member(1L, "email", "password");
    final Coupon coupon = new EmptyCoupon();
    final OrderedItems orderedItems = OrderedItems.createdFromLookUp(List.of(
        new OrderItem(1L, "orderItem1", new Money(10000), "imageUrl1", 1),
        new OrderItem(2L, "orderItem2", new Money(10000), "imageUrl2", 1),
        new OrderItem(3L, "orderItem3", new Money(10000), "imageUrl3", 1)
    ));
    final Money deliveryFee = new Money(3000);

    final Order order = new Order(
        member,
        deliveryFee,
        coupon,
        orderedItems
    );

    //when
    final Long savedId = orderDao.save(order);

    //then
    assertNotNull(savedId);
  }

  @Test
  @DisplayName("findByMemberId() : member Id를 통해 사용자가 주문한 주문 목록들을 조회할 수 있다.")
  void test_findByMemberId() throws Exception {
    //given
    final long memberId = 1L;

    final Member member = new Member(memberId, "email", "password");
    final Coupon coupon = new EmptyCoupon();
    final List<OrderItem> orderItems = List.of(
        new OrderItem(1L, "orderItem1", new Money(10000), "imageUrl1", 1),
        new OrderItem(2L, "orderItem2", new Money(10000), "imageUrl2", 1),
        new OrderItem(3L, "orderItem3", new Money(10000), "imageUrl3", 1)
    );

    when(memberDao.getMemberById(anyLong()))
        .thenReturn(member);

    when(couponDao.findById(anyLong()))
        .thenReturn(Optional.of(coupon));

    when(orderItemDao.findByOrderId(anyLong()))
        .thenReturn(orderItems);

    //when
    final List<Order> orders = orderDao.findByMemberId(memberId);

    //then
    assertEquals(3, orders.size());
  }

  @Test
  @DisplayName("findByOrderId() : order Id를 통해 주문 목록을 조회할 수 있다.")
  void test_findByOrderId() throws Exception {
    //given
    final long orderId = 1L;
    final List<OrderItem> orderItems = List.of(
        new OrderItem(1L, "orderItem1", new Money(10000), "imageUrl1", 1),
        new OrderItem(2L, "orderItem2", new Money(10000), "imageUrl2", 1),
        new OrderItem(3L, "orderItem3", new Money(10000), "imageUrl3", 1)
    );

    when(couponDao.findById(anyLong()))
        .thenReturn(Optional.of(new EmptyCoupon()));

    when(orderItemDao.findByOrderId(anyLong()))
        .thenReturn(orderItems);

    //when
    final Order order = orderDao.findByOrderId(orderId);

    //then
    assertInstanceOf(EmptyCoupon.class, order.getCoupon());
  }

  @Test
  @DisplayName("deleteByOrder() : 주문을 삭제할 수 있다.")
  void test_deleteByOrder() throws Exception {
    //given
    final long orderId = 2L;

    when(couponDao.findById(anyLong()))
        .thenReturn(Optional.of(new EmptyCoupon()));

    final Order order = orderDao.findByOrderId(orderId);

    //when
    orderDao.deleteByOrder(order);

    //then
    assertThatThrownBy(() -> orderDao.findByOrderId(order.getId()))
        .isInstanceOf(OrderException.class)
        .hasMessage(OrderExceptionType.CAN_NOT_FOUND_ORDER.errorMessage());
  }
}
