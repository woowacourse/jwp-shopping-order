package cart.step2.order.service;

import cart.step2.coupon.domain.repository.CouponRepository;
import cart.step2.order.domain.Order;
import cart.step2.order.domain.repository.OrderItemRepository;
import cart.step2.order.domain.repository.OrderRepository;
import cart.step2.order.presentation.dto.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;

    public OrderService(final OrderRepository orderRepository, final OrderItemRepository orderItemRepository, final CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
    }

    @Transactional
    public Long create(final Long memberId, final OrderCreateRequest request) {
        Order order = request.toDomain(memberId);
        return orderRepository.save(order);
    }
}
