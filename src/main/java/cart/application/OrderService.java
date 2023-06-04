package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.domain.Product;
import cart.dto.request.OrderItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderDetailResponse;
import cart.dto.response.OrderItemResponse;
import cart.dto.response.OrderResponse;
import cart.exception.PointNotEnoughException;
import cart.exception.ProductNotFoundException;
import cart.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private static final int POINT_REWARD_PERCENT = 10;

    private final OrderRepository orderRepository;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;

    public OrderService(OrderRepository orderRepository, ProductDao productDao,
                        CartItemDao cartItemDao,
                        MemberDao memberDao) {
        this.orderRepository = orderRepository;
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public Long createOrder(OrderRequest orderRequest, Member member) {
        List<OrderItem> orderItems = createOrderItems(orderRequest);
        Point spendPoint = new Point(orderRequest.getSpendPoint());
        decreaseMemberPoint(member, spendPoint);
        Order order = new Order(null, member, orderItems, spendPoint.getAmount(), LocalDateTime.now());
        Order saveOrder = orderRepository.save(order);
        member.increasePoint(saveOrder.calculateRewardPoint(POINT_REWARD_PERCENT));
        deleteCartItems(member, orderItems);
        memberDao.updateMember(member);
        return saveOrder.getId();
    }

    private void decreaseMemberPoint(Member member, Point spendPoint) {
        validatePoint(member, spendPoint);
        member.decreasePoint(spendPoint);
    }

    private List<OrderItem> createOrderItems(OrderRequest orderRequest) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItem : orderRequest.getOrderItems()) {
            Long productId = orderItem.getProductId();
            int quantity = orderItem.getQuantity();
            Product product = findProduct(productId);
            orderItems.add(new OrderItem(null, product, quantity, product.getPrice() * quantity));
        }
        return orderItems;
    }

    private void validatePoint(Member member, Point point) {
        if (member.hasNotEnoughPoint(point)) {
            throw new PointNotEnoughException("포인트가 부족합니다.");
        }
    }

    private Product findProduct(Long productId) {
        return productDao.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
    }

    private void deleteCartItems(Member member, List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            cartItemDao.deleteByMemberAndProduct(member.getId(), orderItem.getProduct().getId());
        }
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderById(Long orderId, Member member) {
        Order order = orderRepository.findById(orderId);
        order.checkOwner(member);

        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(this::convertToItemResponse)
                .collect(Collectors.toList());

        return new OrderDetailResponse(
                order.getId(),
                order.calculateTotalPrice().getAmount(),
                order.getSpendPoint().getAmount(),
                order.calculateSpendPrice().getAmount(),
                order.getCreatedAt(),
                orderItemResponses
        );
    }

    private OrderItemResponse convertToItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getProduct().getImageUrl(),
                orderItem.getPrice().getAmount(),
                orderItem.getQuantity().getAmount()
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders(Member member) {
        List<Order> orders = orderRepository.findAllByMember(member);
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse convertToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getThumbnailUrl(),
                order.getFirstProductName(),
                order.getOrderItemCount(),
                order.calculateSpendPrice().getAmount(),
                order.getCreatedAt()
        );
    }
}
