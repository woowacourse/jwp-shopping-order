package cart.application.service.order;

import cart.application.repository.CouponRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.application.service.order.dto.OrderResultDto;
import cart.application.service.order.dto.OrderedItemResult;
import cart.application.service.order.dto.UsedCoupon;
import cart.domain.coupon.Coupon;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.ui.MemberAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderReadService {

    private final OrderRepository orderRepository;
    private final OrderedItemRepository orderedItemRepository;
    private final CouponRepository couponRepository;

    public OrderReadService(OrderRepository orderRepository, OrderedItemRepository orderedItemRepository, CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.couponRepository = couponRepository;
    }

    public List<OrderResultDto> findOrdersByMember(MemberAuth memberAuth) {
        List<OrderResultDto> orderResults = new ArrayList<>();
        List<Order> orders = orderRepository.findOrdersByMemberId(memberAuth.getId());
        for (Order order : orders) {
            List<OrderedItemResult> orderedItemResults = makeOrderItemResults(order);
            List<UsedCoupon> usedCoupons = makeUsedCoupons(order);
            orderResults.add(new OrderResultDto(order.getId(), orderedItemResults, usedCoupons, order.getPoint(), order.getPaymentPrice()));
        }
        return orderResults;
    }

    public OrderResultDto findOrderBy(long orderId) {
        Order order = orderRepository.findOrderBy(orderId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
        List<OrderedItemResult> orderedItemResults = makeOrderItemResults(order);
        List<UsedCoupon> usedCoupons = makeUsedCoupons(order);
        return new OrderResultDto(order.getId(), orderedItemResults, usedCoupons, order.getPoint(), order.getPaymentPrice());
    }

    private List<OrderedItemResult> makeOrderItemResults(Order order) {
        List<OrderItem> orderItems = orderedItemRepository.findOrderItemsByOrderId(order.getId());
        return orderItems.stream()
                .map(OrderedItemResult::from)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<UsedCoupon> makeUsedCoupons(Order order) {
        List<Coupon> coupons = couponRepository.findUsedCouponByOrderId(order.getId());
        return coupons.stream()
                .map(UsedCoupon::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
