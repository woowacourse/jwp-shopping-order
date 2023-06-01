package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;

    public OrderService(OrderRepository orderRepository, MemberService memberService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
    }

    public Long save(final Member member, final OrderRequest orderRequest) {
        Order order = orderRepository.makeOrder(member, orderRequest.getCartItemIds());
        order.calculatePrice();
        order.validateBill(orderRequest.getTotalItemPrice(),
                orderRequest.getDiscountedTotalItemPrice(),
                orderRequest.getTotalItemDiscountAmount(),
                orderRequest.getTotalMemberDiscountAmount(),
                orderRequest.getShippingFee());

        memberService.update(member, orderRequest.getTotalPrice());
        return orderRepository.save(order, orderRequest.getCartItemIds()) ;
    }
}
