package cart.application;

import cart.domain.Member;
import cart.domain.Orders;
import cart.domain.OrdersTaker;
import cart.domain.couponissuer.CouponIssuerImpl;
import cart.dto.CouponResponse;
import cart.dto.OrdersRequest;
import cart.dto.OrdersResponse;
import cart.exception.OrdersException;
import cart.repository.OrdersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrdersTaker ordersTaker;
    private final CouponIssuerImpl couponIssuerImpl;

    public OrdersService(OrdersRepository ordersRepository, OrdersTaker ordersTaker, CouponIssuerImpl couponIssuerImpl) {
        this.ordersRepository = ordersRepository;
        this.ordersTaker = ordersTaker;
        this.couponIssuerImpl = couponIssuerImpl;
    }

    public Long takeOrders(Member member, final OrdersRequest ordersRequest) {
        final List<Long> cartIds = ordersRequest.getSelectCartIds();
        return ordersTaker.takeOrder(member.getId(), cartIds, makeCouponsList(ordersRequest.getCouponId()));
    }

    private List<Long> makeCouponsList(final Optional<Long> coupons) {
        return coupons.map(List::of).orElseGet(List::of);
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> findMembersAllOrders(final Member member) {
        return ordersTaker.findOrdersWithMember(member);
    }

    @Transactional(readOnly = true)
    public OrdersResponse findOrdersById(final Member member, final long id) {
        return ordersTaker.findOrdersWithId(member, id);
    }

    public CouponResponse confirmOrders(Member member, long id) throws IllegalAccessException {
        Orders orders = ordersRepository.confirmOrders(id).orElseThrow(() -> new OrdersException("해당 주문내역이 없습니다."));
        return CouponResponse.of(couponIssuerImpl.issue(member, orders));
    }

    public void deleteOrders(final long id) {
        ordersRepository.deleteOrders(id);
    }
}
