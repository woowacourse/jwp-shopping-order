package cart.application;

import cart.dao.MemberCouponDao;
import cart.domain.cartItem.CartItem;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupons;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.dto.order.OrderItemRequest;
import cart.dto.order.OrderItemsRequests;
import cart.dto.order.OrderProductRequest;
import cart.entity.OrderEntity;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.OrderCartMismatchException;
import cart.exception.OrderNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberCouponDao memberCouponDao;
    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final MemberCouponDao memberCouponDao, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.memberCouponDao = memberCouponDao;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Long createOrder(OrderItemsRequests request, Member member) {
        clearCartItems(request.getOrderItemRequests(), member);
        List<OrderItem> orderItems = requestToOrderItems(request.getOrderItemRequests(), member);
        return orderRepository.create(orderItems, new OrderEntity(member.getId(), request.getDeliveryFee()));
    }

    private List<OrderItem> requestToOrderItems(final List<OrderItemRequest> orderItemRequests, final Member member) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            MemberCoupons requestCoupons = usedCoupons(member, orderItemRequest);

            OrderProductRequest productRequest = orderItemRequest.getProductRequest();
            orderItems.add(new OrderItem(productRequest.toProduct(), orderItemRequest.getQuantity(), requestCoupons.getCoupons()));
        }
        return orderItems;
    }

    private MemberCoupons usedCoupons(final Member member, final OrderItemRequest request) {
        MemberCoupons requestCoupons = new MemberCoupons(memberCouponDao.findByIds(request.toMemberCouponIds()));
        MemberCoupons memberCoupons = new MemberCoupons(memberCouponDao.findByMemberId(member.getId()));

        if (memberCoupons.isNotContains(requestCoupons)) {
            throw new MemberCouponNotFoundException();
        }

        memberCouponDao.updateCoupon(requestCoupons.getCoupons(), member.getId());
        return requestCoupons;
    }

    private void clearCartItems(final List<OrderItemRequest> orderItemRequests, final Member member) {
        List<CartItem> memberCartItems = cartItemRepository.findByMemberId(member.getId());

        List<Long> orderItemIds = orderItemRequests.stream()
                .map(OrderItemRequest::getCartItemId)
                .collect(Collectors.toList());
        List<CartItem> requestCartItems = cartItemRepository.findByIds(orderItemIds);

        if (requestCartItems.isEmpty() || !memberCartItems.containsAll(requestCartItems)) {
            throw new OrderCartMismatchException();
        }

        cartItemRepository.deleteAll(requestCartItems);
    }

    public List<Order> findAllByMemberId(Long memberId) {
        return orderRepository.findAllByMemberId(memberId);
    }

    public Order findById(Long id, Member member) {
        Optional<Order> findOrder = orderRepository.findById(id);

        if (findOrder.isEmpty()) {
            throw new OrderNotFoundException();
        }

        Order order = findOrder.get();
        order.checkOwner(member.getId());
        return order;
    }
}
