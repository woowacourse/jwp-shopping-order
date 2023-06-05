package cart.service;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.MemberRepository;
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

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
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
        for (CartItem cartItem : cartItems) {
            cartItemRepository.deleteById(cartItem.getId());
        }

        int totalPrice = cartItems.stream().map(CartItem::getProduct).mapToInt(Product::getPrice).sum();
        calculateMemberCredit(member, request, totalPrice);
        return orderId;
    }

    private void calculateMemberCredit(Member member, OrderRequest request, Integer totalPrice) {
        int payMoney = totalPrice - request.getPoint();
        member.usePoint(request.getPoint());
        member.useMoney(payMoney);

        // 10%
        member.addPoint((payMoney / 100) * 10);
        memberRepository.update(member);
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
