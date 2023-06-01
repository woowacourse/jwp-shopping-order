package cart.application;

import cart.dao.CartItemDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Long save(final Member member, final OrderRequest orderRequest) {
        Order order = orderRepository.makeOrder(member, orderRequest.getCartItemIds());
        order.calculatePrice();
        order.validateBill(orderRequest.getTotalItemPrice(),
                orderRequest.getDiscountedTotalItemPrice(),
                orderRequest.getTotalItemDiscountAmount(),
                orderRequest.getTotalMemberDiscountAmount(),
                orderRequest.getShippingFee());

        return orderRepository.save(order, orderRequest.getCartItemIds()) ;
    }
}
