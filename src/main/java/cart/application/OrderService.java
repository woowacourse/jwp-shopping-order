package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import cart.dto.OrderProductResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.ProductResponse;
import cart.exception.ErrorMessage;
import cart.exception.OrderException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(final MemberRepository memberRepository, final OrderRepository orderRepository,
                        final CartItemRepository cartItemRepository) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private static void validateMemberIsOwnerOfOrder(final Member member, final Order order) {
        if (!order.isOwner(member)) {
            throw new OrderException(ErrorMessage.INVALID_CART_ITEM_OWNER);
        }
    }

    @Transactional
    public Long save(Member member, @Valid OrderRequest orderRequest) {
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        List<CartItem> cartItems = makeCartItems(cartItemIds);
        List<OrderProduct> orderProducts = toOrderProduct(cartItems);
        Order order = new Order(
                null,
                orderProducts,
                member,
                orderRequest.getPoint()
        );

        validateMemberIsSameCartItemOwner(cartItems, member);
        validateMemberPointIsGreaterThanUsedPoint(member, orderRequest.getPoint());
        validateUsedPointIsLessThanPrice(orderRequest.getPoint(), order.getTotalPrice(), order.getDeliveryFee());

        accumulatePoint(member, orderRequest, order);

        cartItemRepository.deleteOrderedCartItem(cartItemIds);

        return orderRepository.save(order);
    }

    private void accumulatePoint(final Member member, final OrderRequest orderRequest, final Order order) {
        member.usePoint(orderRequest.getPoint());
        member.addPoint(order.getTotalPrice());
        memberRepository.updatePoint(member);
    }

    private void validateMemberIsSameCartItemOwner(List<CartItem> cartItems, Member member) {
        boolean isNotOwner = cartItems.stream()
                .anyMatch(cartItem -> !cartItem.getMember().equals(member));

        if (isNotOwner) {
            throw new OrderException(ErrorMessage.INVALID_CART_ITEM_OWNER);
        }
    }

    private void validateMemberPointIsGreaterThanUsedPoint(Member member, int point) {
        if (member.getPoint() < point) {
            throw new OrderException(ErrorMessage.INVALID_MEMBER_POINT_LESS_THAN_USED_POINT);
        }
    }

    private void validateUsedPointIsLessThanPrice(int point, int totalPrice, int deliveryFee) {
        if (totalPrice + deliveryFee < point) {
            throw new OrderException(ErrorMessage.INVALID_POINT_MORE_THAN_PRICE);
        }
    }

    private List<CartItem> makeCartItems(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());
    }

    private List<OrderProduct> toOrderProduct(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderProduct(
                        null,
                        cartItem.getProduct(),
                        cartItem.getQuantity()
                ))
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId, member);
        validateMemberIsOwnerOfOrder(member, order);

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderProductResponse> orderProductResponses = order.getOrderProducts().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                orderProductResponses,
                order.getTotalPrice(),
                order.getUsedPoint(),
                order.getDeliveryFee(),
                order.getCreatedAt()
        );
    }

    private OrderProductResponse toProductResponse(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();

        ProductResponse productResponse = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );

        return new OrderProductResponse(
                productResponse,
                orderProduct.getQuantity()
        );
    }

    public List<OrderResponse> findByMember(final Member member) {
        List<Order> orders = orderRepository.findOrdersByMember(member);

        return orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
