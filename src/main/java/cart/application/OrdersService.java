package cart.application;

import cart.domain.*;
import cart.domain.couponissuer.CouponIssuerImpl;
import cart.dto.CouponResponse;
import cart.dto.OrdersRequest;
import cart.dto.OrdersResponse;
import cart.exception.OrdersException;
import cart.repository.CouponRepository;
import cart.repository.OrdersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final CouponRepository couponRepository;
    private final OrdersTaker ordersTaker;
    private final CouponIssuerImpl couponIssuerImpl;

    public OrdersService(OrdersRepository ordersRepository, CouponRepository couponRepository, OrdersTaker ordersTaker, CouponIssuerImpl couponIssuerImpl) {
        this.ordersRepository = ordersRepository;
        this.couponRepository = couponRepository;
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
        return reverse(ordersRepository.findAllOrdersByMember(member).stream()
                .map(orders -> makeResponse(member, Optional.ofNullable(orders)))
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public OrdersResponse findOrdersById(final Member member, final long id) {
        return makeResponse(member, ordersRepository.findOrdersById(id));
    }

    public CouponResponse confirmOrders(Member member, long id) throws IllegalAccessException {
        Orders orders = ordersRepository.confirmOrders(id).orElseThrow(() -> new OrdersException("해당 주문내역이 없습니다."));
        return CouponResponse.of(couponIssuerImpl.issue(member, orders));
    }

    public void deleteOrders(final long id) {
        ordersRepository.deleteOrders(id);
    }
    private List<OrdersResponse> reverse(List<OrdersResponse> responses) {
        Collections.reverse(responses);
        return responses;
    }
    public OrdersResponse makeResponse(final Member member, final Optional<Orders> orders) {
        if (orders.isEmpty()) {
            return OrdersResponse.noOrdersMembersResponse();
        }
        List<CartItem> ordersItem = ordersRepository.findCartItemByOrdersIds(member, orders.get().getId());
        List<Coupon> coupons = couponRepository.findByOrdersId(orders.get().getId());
        return OrdersResponse.of(
                orders.get(),
                ordersItem,
                ordersItem.stream().map(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity()).reduce(0,Integer::sum),
                coupons
        );
    }
}
