package cart.repository;

import cart.dao.*;
import cart.domain.order.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cart.fixture.MemberFixture.라잇;
import static cart.fixture.MemberFixture.라잇_엔티티;
import static cart.fixture.OrderFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private CouponDao couponDao;

    @Mock
    private MemberCouponDao memberCouponDao;

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private OrderRepository orderRepository;

    @Test
    void 저장한다() {
        when(orderDao.save(주문1_아이디_널_엔티티)).thenReturn(1L);

        orderRepository.save(주문1_아이디_널);

        verify(orderItemDao).saveAll(List.of(주문상품1_아이디_널_엔티티, 주문상품2_아이디_널_엔티티));
    }

    @Test
    void 회원으로_조회한다() {
        when(orderDao.findByMemberId(라잇.getId())).thenReturn(List.of(주문1_엔티티));
        when(orderItemDao.findByOrderIds(List.of(주문1_엔티티.getId()))).thenReturn(List.of(주문상품1_엔티티, 주문상품2_엔티티));
        when(memberCouponDao.findByIds(anyList())).thenReturn(new ArrayList<>());
        when(couponDao.findByIds(anyList())).thenReturn(new ArrayList<>());

        List<Order> orders = orderRepository.findAllByMember(라잇);

        assertThat(orders).contains(주문1);
    }

    @Test
    void 아이디로_조회한다() {
        when(orderDao.findById(주문1.getId())).thenReturn(Optional.of(주문1_엔티티));
        when(orderItemDao.finByOrderId(주문1.getId())).thenReturn(List.of(주문상품1_엔티티, 주문상품2_엔티티));
        when(memberDao.findById(라잇.getId())).thenReturn(Optional.of(라잇_엔티티));
        when(memberCouponDao.findById(null)).thenReturn(Optional.empty());

        Order order = orderRepository.findById(주문1.getId());

        assertThat(order).isEqualTo(주문1);
    }

    @Test
    void 삭제한다() {
        orderRepository.delete(주문1);

        verify(orderItemDao).deleteByOrderId(주문1.getId());
        verify(orderDao).deleteById(주문1.getId());
    }
}
