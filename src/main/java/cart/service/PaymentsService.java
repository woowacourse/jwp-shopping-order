package cart.service;

import cart.domain.*;
import cart.dto.OrderReqeust;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService {
    private final CartItemService cartItemService;

    private final OrderRepository orderRepository;

    public PaymentsService(CartItemService cartItemService, OrderRepository orderRepository) {
        this.cartItemService = cartItemService;
        this.orderRepository = orderRepository;
    }

    public Long order(Member member, OrderReqeust orderReqeust) {
        Order order = cartItemService.order(member, orderReqeust);
        Long id = orderRepository.insert(member, order);

        for (Long cartItemId : orderReqeust.getCartItemIds()) {
            cartItemService.remove(member, cartItemId);
        }

        return id;
    }
}


