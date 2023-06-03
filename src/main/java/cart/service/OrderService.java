package cart.service;

import cart.dao.*;
import cart.domain.*;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.InvalidCardException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;
    private final ShoppingOrderDao shoppingOrderDao;
    private final OrderedItemDao orderedItemDao;

    public OrderService(CartItemDao cartItemDao, ProductDao productDao, MemberDao memberDao, ShoppingOrderDao shoppingOrderDao, OrderedItemDao orderedItemDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
        this.shoppingOrderDao = shoppingOrderDao;
        this.orderedItemDao = orderedItemDao;
    }

    public Long createOrder(Member member, OrderRequest orderRequest) {
        Card card = new WootecoCard(orderRequest.getCardNumber(), orderRequest.getCvc());
        if (!card.isValid()) {
            throw new InvalidCardException("카드 정보가 유효하지 않습니다");
        }
        Order order = new Order(member, LocalDateTime.now(), new Point(orderRequest.getPoint()));
        Long orderId = shoppingOrderDao.save(order);
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        CartItems cartItems = getCartItems(cartItemIds);
        cartItems.checkOwner(member);
        saveOrderItems(orderId, cartItems);
        long totalOrderPrice = cartItems.calculateTotalPrice();
        updateMemberPoints(member, orderRequest, totalOrderPrice);
        cartItemIds.forEach(cartItemDao::deleteById);

        return orderId;
    }

    private void updateMemberPoints(Member member, OrderRequest orderRequest, long totalOrderPrice) {
        Point currentPoint = member.getPoint();
        Point savingPoint = PointEarningPolicy.calculateSavingPoints(totalOrderPrice - orderRequest.getPoint());
        Point updated = new Point(currentPoint.getValue() - orderRequest.getPoint() + savingPoint.getValue());
        memberDao.updatePoints(updated.getValue(), member);
    }

    private void saveOrderItems(Long orderId, CartItems cartItems) {
        cartItems.getCartItems().forEach(cartItem -> {
            Integer quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();
            orderedItemDao.save(product.getId(), orderId, quantity);
        });
    }

    private CartItems getCartItems(List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemIds.stream()
                .map(cartItemId -> cartItemDao.findById(cartItemId).orElseThrow(() -> new CartItemException("장바구니 목록에서 조회할 수 없습니다")))
                .collect(Collectors.toList());
        return new CartItems(cartItems);
    }

    public List<OrderResponse> findAll(Long id) {
        List<Order> orders = shoppingOrderDao.findAll(id);

        List<Order> groupedOrders = groupOrderList(orders);
        groupedOrders.forEach(Order::calculateSavedPoint);
        List<OrderResponse> orderResponses = groupedOrders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
        return orderResponses;
    }

    private List<Order> groupOrderList(List<Order> orders) {
        Map<List<Object>, List<Order>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(order -> Arrays.asList(order.getId(), order.getMember(), order.getOrderedAt(), order.getUsedPoint())));

        return groupedOrders.values().stream().map(group -> {
            Order firstOrder = group.get(0);
            OrderedItems mergedItems = group.stream()
                    .map(Order::getOrderedItems)
                    .reduce(new OrderedItems(new ArrayList<>()), OrderedItems::merge);

            return new Order(firstOrder.getId(), firstOrder.getMember(), firstOrder.getOrderedAt(), firstOrder.getUsedPoint(), mergedItems);
        }).collect(Collectors.toList());
    }

    public OrderResponse findById(Member member, Long id) {
        Member orderOwner = memberDao.findByOrderId(id).orElseThrow(AuthenticationException::new);
        if (!orderOwner.equals(member)) {
            throw new IllegalArgumentException("로그인한 사용자의 주문 목록이 아닙니다");
        }

        List<Order> orders = shoppingOrderDao.findById(id);
        Order firstOrder = orders.get(0);
        OrderedItems mergedItems = orders.stream()
                .map(Order::getOrderedItems)
                .reduce(new OrderedItems(new ArrayList<>()), OrderedItems::merge);
        Order order = new Order(firstOrder.getId(), firstOrder.getMember(), firstOrder.getOrderedAt(), firstOrder.getUsedPoint(), mergedItems);
        order.calculateSavedPoint();

        return OrderResponse.of(order);
    }
}
