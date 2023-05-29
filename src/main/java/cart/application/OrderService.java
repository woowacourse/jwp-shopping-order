package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.PointPolicy;
import cart.dto.CartItemRequest;
import cart.dto.CartPointsResponse;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemResponse;
import cart.dto.OrderResponse;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public OrderService(final OrderDao orderDao, final OrderItemDao orderItemDao, final CartItemDao cartItemDao, final ProductDao productDao, final MemberDao memberDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    public Long createOrder(final OrderCreateRequest orderCreateRequest, final Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberIdAndChecked(member.getId());
        final List<CartItemRequest> requests = orderCreateRequest.getCartItems();

        validateLegalOrder(cartItems, requests);

        final List<CartItem> cartItemsByRequest = toCartItems(member, requests);

        final List<Long> cartItemIds = cartItemsByRequest.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        cartItemDao.deleteAll(cartItemIds);

        final Order order = new Order(orderCreateRequest.getUsedPoints(), cartItemsByRequest);
        order.validatePoints(member.getPoints());

        final Long id = orderDao.createOrder(orderCreateRequest.getUsedPoints(), cartItemsByRequest, PointPolicy.getSavingRate(), member);

        updateMember(member, order);

        return id;
    }

    private void validateLegalOrder(final List<CartItem> cartItems, final List<CartItemRequest> requests) {
        for (final CartItem cartItem : cartItems) {
            iterateRequests(requests, cartItem);
        }
    }

    private List<CartItem> toCartItems(final Member member, final List<CartItemRequest> requests) {
        return requests.stream()
                .map(cartItemRequest -> new CartItem(cartItemRequest.getId(), cartItemRequest.getQuantity(),
                        productDao.getProductById(cartItemRequest.getProductId()),
                        member,
                        true
                )).collect(Collectors.toList());
    }

    private void iterateRequests(final List<CartItemRequest> requests, final CartItem cartItem) {
        for (final CartItemRequest request : requests) {
            compareEachCartItemIfIdEquals(cartItem, request);
        }
    }

    private void compareEachCartItemIfIdEquals(final CartItem cartItem, final CartItemRequest request) {
        if (cartItem.getId().equals(request.getId())) {
            compareEachCartItem(cartItem, request);
        }
    }

    private void compareEachCartItem(final CartItem cartItem, final CartItemRequest request) {
        if (isInvalidProduct(cartItem, request) || isInvalidQuantity(cartItem, request) || isNotChecked(cartItem)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isInvalidProduct(final CartItem cartItem, final CartItemRequest request) {
        return !cartItem.getProduct().getId().equals(request.getProductId());
    }

    private boolean isInvalidQuantity(final CartItem cartItem, final CartItemRequest request) {
        return cartItem.getQuantity() != request.getQuantity();
    }

    private boolean isNotChecked(final CartItem cartItem) {
        return !cartItem.isChecked();
    }

    private void updateMember(final Member member, final Order order) {
        final int savingPoints = PointPolicy.calculateSavingPoints(order.getPoints(), order.getCartItems());
        final Member updatedMember = member.updatePoints(savingPoints, order.getPoints());
        memberDao.updateMember(updatedMember);
    }

    public OrderResponse findById(final Long orderId, final Member member) {
        final OrderEntity orderEntity = orderDao.findById(orderId, member.getId());
        final List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderId);
        final List<OrderItemResponse> orderItemResponses = parseOrderItemEntitiesToResponses(orderItemEntities);
        return new OrderResponse(orderEntity.getId(), orderEntity.getSavingRate(), orderEntity.getPoints(), orderItemResponses);
    }

    private List<OrderItemResponse> parseOrderItemEntitiesToResponses(final List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(orderItemEntity -> new OrderItemResponse(
                        orderItemEntity.getProductId(),
                        orderItemEntity.getProductName(),
                        orderItemEntity.getProductPrice(),
                        orderItemEntity.getProductQuantity(),
                        orderItemEntity.getProductImageUrl()
                )).collect(Collectors.toList());
    }

    public List<OrderResponse> findAll(final Member member) {
        List<OrderEntity> orderEntities = orderDao.findAll(member.getId());

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (final OrderEntity orderEntity : orderEntities) {
            List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderEntity.getId());
            final List<OrderItemResponse> orderItemResponses = parseOrderItemEntitiesToResponses(orderItemEntities);
            orderResponses.add(new OrderResponse(
                    orderEntity.getId(),
                    orderEntity.getSavingRate(),
                    orderEntity.getPoints(),
                    orderItemResponses
            ));
        }
        return orderResponses;
    }

    public CartPointsResponse calculatePoints(final Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberIdAndChecked(member.getId());
        final int savingPoints = PointPolicy.calculateSavingPoints(0, cartItems);

        return new CartPointsResponse(PointPolicy.getSavingRate(), savingPoints);
    }
}
