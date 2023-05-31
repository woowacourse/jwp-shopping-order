package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.dto.OrderRequest;
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
        validateUsedPointIsLessThanPrice(orderRequest.getPoint(), order.getTotalPrice());

        accumulatePoint(member, orderRequest, order);

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

    private void validateUsedPointIsLessThanPrice(int point, int totalPrice) {
        if (totalPrice < point) {
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
}
