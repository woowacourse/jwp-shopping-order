package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.orderproduct.Order;
import cart.domain.orderproduct.OrderProduct;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderProductDto;
import cart.dto.OrderRequest;
import cart.exception.MemberNotFoundException;
import cart.exception.point.InvalidPointUseException;
import cart.exception.point.PointAbusedException;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    private final OrderRepository orderRepository;

    public OrderService(final MemberDao memberDao, final CartItemDao cartItemDao, final OrderRepository orderRepository) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.orderRepository = orderRepository;
    }


    @Transactional
    public Long order(final Member member, final OrderRequest request) {
        final Member findMember = memberDao.findByEmail(member.getEmailValue())
                .orElseThrow(() -> new MemberNotFoundException(member.getEmailValue()));
        if (findMember.getPointValue() < request.getPoint()) {
            throw new PointAbusedException(member.getPointValue(), request.getPoint());
        }
        final List<CartItem> cartItems1 = cartItemDao.getCartItemsByIds(request.getCartItemIds());
        final CartItems cartItems = new CartItems(cartItems1);
        final int totalPrice = cartItems.calculateTotalPrice();
        if (totalPrice < request.getPoint()) {
            throw new InvalidPointUseException(totalPrice, request.getPoint());
        }
        final Member updatedMember = findMember.updatePoint(new MemberPoint(request.getPoint()), totalPrice);
        memberDao.update(updatedMember);

        return orderRepository.save(cartItems, updatedMember, new MemberPoint(request.getPoint()));
    }

    public OrderDetailResponse getOrderDetail(final Member member, final Long orderId) {
        final Order order = orderRepository.findOrderById(orderId);
        final List<OrderProduct> orderProducts = orderRepository.findAllOrderProductsByOrderId(orderId);

        return new OrderDetailResponse(
                order.getId(),
                orderProducts.stream()
                        .mapToInt(orderProduct -> orderProduct.getProductPriceValue() * orderProduct.getQuantityValue())
                        .sum(),
                order.getUsedPointValue(),
                order.getCreatedAt(),
                orderProducts.stream()
                        .map(m -> new OrderProductDto(
                                m.getProductId(),
                                m.getProductNameValue(),
                                m.getProductPriceValue(),
                                m.getProductImageUrlValue(),
                                m.getQuantityValue()
                        )).collect(Collectors.toList())
        );
    }

    public List<OrderDetailResponse> getAllOrderDetails(final Member member) {
        final List<Order> orders = orderRepository.findOrdersByMemberId(member.getId());
//        final List<OrderProduct> orderProducts = orderRepository.findAllOrderProductsByMemberId(member.getId());
        final List<OrderDetailResponse> result = new ArrayList<>();
        for (Order order : orders) {
            final List<OrderProduct> orderProducts = orderRepository.findAllOrderProductsByOrderId(order.getId());
            final OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
                    order.getId(),
                    orderProducts.stream()
                            .mapToInt(orderProduct -> orderProduct.getProductPriceValue() * orderProduct.getQuantityValue())
                            .sum(),
                    order.getUsedPointValue(),
                    order.getCreatedAt(),
                    orderProducts.stream()
                            .map(m -> new OrderProductDto(
                                    m.getProductId(),
                                    m.getProductNameValue(),
                                    m.getProductPriceValue(),
                                    m.getProductImageUrlValue(),
                                    m.getQuantityValue())
                            ).collect(Collectors.toList())
            );
            result.add(orderDetailResponse);
        }
        return result;
    }
}
