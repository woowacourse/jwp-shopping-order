package cart.application;

import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.UsedPoint;
import cart.domain.product.ProductPrice;
import cart.exception.business.point.InvalidPointUseException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.ui.dto.order.OrderDetailResponse;
import cart.ui.dto.order.OrderProductDto;
import cart.ui.dto.order.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository,
                        final MemberRepository memberRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
    }


    @Transactional
    public Long order(final Member member, final OrderRequest request) {
        final Member findMember = memberRepository.findByEmail(member.getEmailValue());
        final CartItems cartItems = new CartItems(cartItemRepository.findAllByIds(request.getCartItemIds()));
        cartItems.checkOwner(findMember);
        final int totalPrice = cartItems.getTotalPrice();
        validateInvalidPointUse(request, cartItems, totalPrice);
        final Member updatedMember = findMember.updatePoint(new MemberPoint(request.getPoint()), new ProductPrice(totalPrice));
        memberRepository.save(updatedMember);
        final Long orderId = orderRepository.save(cartItems, updatedMember, new UsedPoint(request.getPoint()));
        cartItemRepository.deleteByIds(cartItems.getCartItemIds());
        return orderId;
    }

    private void validateInvalidPointUse(final OrderRequest request, final CartItems cartItems, final int totalPrice) {
        if (totalPrice + cartItems.getDeliveryFee() < request.getPoint()) {
            throw new InvalidPointUseException(totalPrice, request.getPoint());
        }
    }

    public OrderDetailResponse getOrderDetail(final Member member, final Long orderId) {
        final Order order = orderRepository.findById(orderId);
        order.checkOwner(member);
        final List<OrderProduct> orderProducts = orderRepository.findAllByOrderId(orderId);
        return getOrderDetailResponse(order, orderProducts);
    }

    public List<OrderDetailResponse> getAllOrderDetails(final Member member) {
        final List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));
        final List<OrderDetailResponse> result = new ArrayList<>();
        for (Order order : orders) {
            final List<OrderProduct> orderProducts = orderRepository.findAllByOrderId(order.getId());
            result.add(getOrderDetailResponse(order, orderProducts));
        }
        return result;
    }

    @Transactional
    public void deleteByIds(final Member member, final List<Long> orderIds) {
        final List<Order> orders = orderRepository.findAllByOrderIds(orderIds);
        orders.forEach(order -> order.checkOwner(member));
        orderRepository.deleteAll(orders);
    }

    @Transactional
    public void deleteAll(final Member member) {
        final List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));
        orderRepository.deleteAll(orders);
    }

    private OrderDetailResponse getOrderDetailResponse(final Order order, final List<OrderProduct> orderProducts) {
        return new OrderDetailResponse(
                order.getId(),
                orderProducts.stream()
                        .mapToInt(orderProduct -> orderProduct.getProductPriceValue() * orderProduct.getQuantityValue())
                        .sum(),
                order.getUsedPointValue(),
                order.getDeliveryFeeValue(),
                order.getOrderedAt(),
                orderProducts.stream()
                        .map(orderProduct -> new OrderProductDto(
                                orderProduct.getProductId(),
                                orderProduct.getProductNameValue(),
                                orderProduct.getProductPriceValue(),
                                orderProduct.getProductImageUrlValue(),
                                orderProduct.getQuantityValue()
                        )).collect(Collectors.toList())
        );
    }
}
