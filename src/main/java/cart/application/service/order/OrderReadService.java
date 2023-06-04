package cart.application.service.order;

import cart.application.repository.order.OrderRepository;
import cart.application.service.order.dto.OrderResultDto;
import cart.domain.member.Member;
import cart.domain.order.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderReadService {

    private final OrderRepository orderRepository;

    public OrderReadService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResultDto> findAllByMember(final Member member) {
        final List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        return OrderResultDto.from(orders);
    }

    public OrderResultDto findMemberOrderByOrderId(final Member member, final Long orderId) {
        final Order order = orderRepository.findByOrderId(member.getId(), orderId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 주문번호가 없습니다."));

        return OrderResultDto.from(order);
    }

}
