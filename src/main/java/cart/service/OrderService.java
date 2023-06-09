package cart.service;

import cart.dao.*;
import cart.domain.*;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.InvalidCardException;
import cart.exception.OrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
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
        validateCard(orderRequest);

        Long orderId = saveOrder(member, orderRequest);

        CartItems cartItems = getCartItems(member, orderRequest);

        saveOrderItems(orderId, cartItems);

        long totalOrderPrice = cartItems.calculateTotalPrice();
        updateMemberPoints(member, orderRequest, totalOrderPrice);

        return orderId;
    }

    private void validateCard(OrderRequest orderRequest) {
        Card card = new WootecoCard(orderRequest.getCardNumber(), orderRequest.getCvc());
        if (card.isInValid()) {
            throw new InvalidCardException("카드 정보가 유효하지 않습니다");
        }
    }

    private Long saveOrder(Member member, OrderRequest orderRequest) {
        Order order = new Order(member, LocalDateTime.now(), new Point(orderRequest.getPoint()));
        return shoppingOrderDao.save(order);
    }

    private CartItems getCartItems(Member member, OrderRequest orderRequest) {
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        CartItems cartItems = findCartItems(cartItemIds);
        cartItems.checkOwner(member);
        cartItemIds.forEach(cartItemDao::deleteById);
        return cartItems;
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

    private CartItems findCartItems(List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemIds.stream()
                .map(cartItemId -> cartItemDao.findById(cartItemId).orElseThrow(()
                        -> new CartItemException(cartItemId + "를 장바구니 목록에서 조회할 수 없습니다")))
                .collect(Collectors.toList());
        return new CartItems(cartItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(Long id) {
        List<Order> orders = shoppingOrderDao.findAll(id);

        Orders groupedOrders = Orders.of(orders);
        groupedOrders.calculateSavedPoint();

        return groupedOrders.getOrders().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Member member, Long id) {
        Member orderOwner = memberDao.findByOrderId(id).orElseThrow(AuthenticationException::new);
        if (!orderOwner.equals(member)) {
            throw new OrderException("로그인한 사용자의 주문 목록이 아닙니다");
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
