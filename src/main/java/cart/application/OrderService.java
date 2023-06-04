package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.PointPolicy;
import cart.dto.DtoMapper;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderCreateRequest;
import cart.dto.response.CartPointsResponse;
import cart.dto.response.OrderResponse;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public OrderService(
            final OrderDao orderDao,
            final OrderItemDao orderItemDao,
            final CartItemDao cartItemDao,
            final ProductDao productDao,
            final MemberDao memberDao
    ) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    public Long createOrder(final OrderCreateRequest orderCreateRequest, final Member member) {
        List<Long> orderedCartItemIds = orderCreateRequest.getCartItemIds();
        List<CartItem> cartItemsFromRequest = convertToCartItems(member, orderCreateRequest.getCartItems());

        validateOrderedCartItems(orderedCartItemIds, cartItemsFromRequest);

        Order order = Order.of(orderCreateRequest.getUsedPoints(), cartItemsFromRequest);
        order.validatePoints(member.getPoints());
        saveMemberPoints(member, order);

        cartItemDao.deleteAll(orderedCartItemIds);
        return orderDao.createOrder(
                orderCreateRequest.getUsedPoints(),
                cartItemsFromRequest,
                PointPolicy.getSavingRate(),
                member
        );
    }

    private void validateOrderedCartItems(List<Long> orderedCartItemIds, List<CartItem> cartItemsFromRequest) {
        CartItems requestedCartItems = CartItems.from(cartItemsFromRequest);
        CartItems serverCartItems = CartItems.from(cartItemDao.findByIds(orderedCartItemIds));
        requestedCartItems.validateAllCartItemsOrderedLegally(serverCartItems);
    }

    private List<CartItem> convertToCartItems(final Member member, final List<CartItemRequest> requests) {
        return requests.stream()
                .map(cartItemRequest -> CartItem.of(
                        cartItemRequest.getId(),
                        cartItemRequest.getQuantity(),
                        productDao.findProductById(cartItemRequest.getProductId()),
                        member,
                        true
                )).collect(Collectors.toList());
    }

    private void saveMemberPoints(final Member member, final Order order) {
        final int savingPoints = PointPolicy.calculateSavingPoints(order.getPoints(), order.getCartItems());
        final Member updatedMember = member.updatePoints(savingPoints, order.getPoints());
        memberDao.updateMember(updatedMember);
    }

    public OrderResponse findById(final Long orderId, final Member member) {
        final OrderEntity orderEntity = orderDao.findById(orderId, member.getId());
        final List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderId);

        return DtoMapper.convertToOrderResponse(orderEntity, orderItemEntities);
    }

    public List<OrderResponse> findAll(final Member member) {
        List<OrderEntity> orderEntities = orderDao.findAll(member.getId());

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (final OrderEntity orderEntity : orderEntities) {
            List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderEntity.getId());
            orderResponses.add(DtoMapper.convertToOrderResponse(orderEntity, orderItemEntities));
        }
        return orderResponses;
    }

    public CartPointsResponse calculatePoints(final Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberIdAndChecked(member.getId());
        final int savingPoints = PointPolicy.calculateSavingPoints(0, cartItems);

        return new CartPointsResponse(PointPolicy.getSavingRate(), savingPoints);
    }
}

