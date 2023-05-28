package cart.service;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.OrderItem;
import cart.domain.Orders;
import cart.dto.OrderItemIdDto;
import cart.dto.OrderSaveRequest;
import cart.dto.OrdersDto;
import cart.exception.MemberNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    public OrderService(final CartItemRepository cartItemRepository, final OrderRepository orderRepository,
                        final MemberRepository memberRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
    }

    public Long order(final Long memberId, final OrderSaveRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final List<CartItem> findCartItems = getCartItems(memberId, request);
        final List<OrderItem> orderItems = findCartItems.stream()
                .map(it -> {
                    String productName = it.getProduct().getName();
                    long price = it.getProduct().getPrice();
                    String imageUrl = it.getProduct().getImageUrl();
                    Integer quantity = it.getQuantity();

                    return new OrderItem(productName, price, imageUrl, quantity);
                })
                .collect(Collectors.toList());

        final Orders newOrders = new Orders(null, member, orderItems);

        Long savedOrderId = orderRepository.save(newOrders).getId();
        for (CartItem findCartItem : findCartItems) {
            cartItemRepository.deleteById(findCartItem.getId(), memberId);
        }
        return savedOrderId;
    }

    private List<CartItem> getCartItems(final Long memberId, final OrderSaveRequest request) {
        final List<Long> orderItemIds = request.getOrderItems().stream()
                .map(OrderItemIdDto::getId)
                .collect(Collectors.toList());
        return cartItemRepository.findAllByMemberIdAndCartItemIds(memberId,
                orderItemIds);
    }

    public OrdersDto findByOrderId(final Long memberId, final Long orderId) {
        return new OrdersDto(orderRepository.findByOrderIdAndMemberId(orderId, memberId));
    }

    public List<OrdersDto> findAllBy(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(OrdersDto::new)
                .collect(Collectors.toList());
    }
}
