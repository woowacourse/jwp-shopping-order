package cart.application;

import cart.domain.Member;
import cart.domain.OrdersTaker;
import cart.domain.couponissuer.CouponIssuer;
import cart.domain.couponissuer.Issuer;
import cart.dto.CouponResponse;
import cart.dto.OrdersRequest;
import cart.dto.OrdersResponse;
import cart.repository.CouponRepository;
import cart.repository.OrdersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrdersTaker ordersTaker;
    private final CouponRepository couponRepository;

    public OrdersService(OrdersRepository ordersRepository, OrdersTaker ordersTaker, CouponRepository couponRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersTaker = ordersTaker;
        this.couponRepository = couponRepository;
    }

    @Transactional
    public Long takeOrders(Member member, final OrdersRequest ordersRequest) {
        final List<Long> cartIds = ordersRequest.getSelectCartIds();
        if (ordersRequest.isNoCoupon()) {
            return ordersTaker.takeOrder(member.getId(), cartIds, List.of());
        }
        final List<Long> coupons = List.of(ordersRequest.getCouponId());
        return ordersTaker.takeOrder(member.getId(), cartIds, coupons);
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> findMembersAllOrders(final Member member) {
        return ordersTaker.findOrdersWithMember(member);
    }

    @Transactional(readOnly = true)
    public OrdersResponse findOrdersById(final Member member, final long id) {
        return ordersTaker.findOrdersWithId(member, id);
    }

    @Transactional
    public CouponResponse confirmOrders(Member member, long id) {
        CouponIssuer couponIssuer = new Issuer(couponRepository);
        return CouponResponse.of(couponIssuer.issue(member, ordersRepository.confirmOrders(id).get()));
    }

    @Transactional
    public void deleteOrders(final long id) {
        ordersRepository.deleteOrders(id);
    }
}
