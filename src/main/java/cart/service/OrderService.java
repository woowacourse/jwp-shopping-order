package cart.service;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.OrderRepository;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.exception.OrderException;
import cart.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(PaymentService paymentService, OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(Member member) {
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());

        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderException.NotFound(id));

        return OrderMapper.toResponse(order);
    }

    public Long order(Member member, OrderRequest request) {
        List<CartItem> cartItems = getCartItems(request);
        Long orderId = orderRepository.create(member, request.getPoint(), cartItems);

        deleteCartItems(cartItems);

        int totalPrice = cartItems.stream().map(CartItem::getProduct).mapToInt(Product::getPrice).sum();
        paymentService.payByMember(member, request.getPoint(), totalPrice);

        return orderId;
    }

    private void deleteCartItems(List<CartItem> cartItems) {
        cartItems.stream()
                .map(CartItem::getId)
                .forEach(cartItemRepository::deleteById);
    }

    private List<CartItem> getCartItems(OrderRequest request) {
        List<CartItem> cartItems = new ArrayList<>();

        for (Long id : request.getCartIds()) {
            CartItem cartItem = cartItemRepository.findById(id)
                    .orElseThrow(() -> new OrderException.NotFound(id));
            cartItems.add(cartItem);
        }
        return cartItems;
    }
}
