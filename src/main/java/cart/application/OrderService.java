package cart.application;

import cart.domain.*;
import cart.dto.AllOrderResponse;
import cart.dto.OrderCartItemRequest;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MemberCouponRepository memberCouponRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(final MemberCouponRepository memberCouponRepository, final CartItemRepository cartItemRepository, final OrderRepository orderRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long createOrder(final Member member, final OrderRequest request) {
        MemberCoupon memberCoupon = findCouponIfExist(request.getCouponId());
        CartItems cartItems = new CartItems(findCartItemsRequest(request));
        CartItems selectedCartItems = request.toCartItems(member);

        // TODO: 6/4/23 이 과정이 하나의 도메인 로직으로 들어가도 될듯
        cartItems.checkStatus(selectedCartItems, member);
        memberCoupon.checkExpired();
        memberCoupon.checkOwner(member);
        Order order = cartItems.order(member, memberCoupon);
        MemberCoupon usedMemberCoupon = memberCoupon.use();

        memberCouponRepository.update(usedMemberCoupon);
        cartItemRepository.deleteAll(cartItems.getCartItems());
        return orderRepository.save(order);
    }

    private MemberCoupon findCouponIfExist(final Long memberCouponId) {
        if (memberCouponId == null) {
            return new EmptyMemberCoupon();
        }
        return memberCouponRepository.findById(memberCouponId);
    }

    private List<CartItem> findCartItemsRequest(final OrderRequest request) {
        List<Long> currentCartIds = request.getProducts().stream()
                .map(OrderCartItemRequest::getCartItemId)
                .collect(Collectors.toList());
        return cartItemRepository.findByIds(currentCartIds);
    }

    @Transactional(readOnly = true)
    public AllOrderResponse findAllOrderByMember(final Member member) {
        List<Order> allOrders = orderRepository.findAllByMember(member);

        for (Order order : allOrders) {
            order.checkOwner(member);
        }

        return AllOrderResponse.from(allOrders);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderByIdAndMember(final Long id, final Member member) {
        Order order = orderRepository.findById(id);

        order.checkOwner(member);

        return OrderDetailResponse.from(order);
    }

    @Transactional(readOnly = true)
    public void cancelOrder(final Long orderId, final Member member) {
        Order order = orderRepository.findById(orderId);

        Order canceledOrder = order.cancel();

        memberCouponRepository.update(canceledOrder.getMemberCoupon());
        orderRepository.delete(canceledOrder);
    }
}
