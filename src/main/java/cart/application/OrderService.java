package cart.application;

import cart.dao.cartitem.CartItemDao;
import cart.dao.order.OrderDao;
import cart.dao.order.OrderItemDao;
import cart.dao.point.PointDao;
import cart.dao.product.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderPoint;
import cart.domain.point.Point;
import cart.domain.point.PointPolicy;
import cart.dto.order.OrderItemResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.exception.customexception.CartItemNotFoundException;
import cart.exception.customexception.OrderNotFoundException;
import cart.exception.customexception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final PointDao pointDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderPoint orderPoint;

    public OrderService(CartItemDao cartItemDao, ProductDao productDao, PointDao pointDao, OrderDao orderDao, OrderItemDao orderItemDao, PointPolicy pointPolicy) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.pointDao = pointDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderPoint = new OrderPoint(pointPolicy);
    }

    @Transactional
    public Long orderCartItems(Member member, OrderRequest orderRequest) {
        // 0. 현재 주문 시간 구하기
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        Timestamp createdAt = new Timestamp(calendar.getTimeInMillis());

        // 1. 총 가격, 쓸 포인트, CartIds 꺼내기
        Long totalPrice = orderRequest.getTotalPrice();
        Long usePoint = orderRequest.getPoint();
        List<Long> cartIds = orderRequest.getCartIds();
        // 2. 포인트 조회[남은 포인트가 0초과 && 만료기한이 지나지 않음] 후 [보유 포인트가 사용포인트 보다 적거나 같다면 예외] 남은 포인트 업데이트
        usePoint(member, createdAt, usePoint);

        // 3. OrderPoint 를 통해 새로운 포인트와 유효기간 계산 후 삽입 [아이디 반환] [총 가격보다, 쓴 포인트가 많으면 예외]
        Point newPoint = orderPoint.earnPoint(member, usePoint, totalPrice, createdAt);
        Long pointId = pointDao.createPoint(newPoint);

        // 4. CartIds 로 CartItems 조회 [회원의 소유가 아니거나 재고보다 많은 주문량 시 예외]
        List<CartItem> cartItems = findCartItems(member, cartIds);

        // 5. 상품_재고_업데이트
        updateProductStocks(cartItems);

        // 6. 장바구니에서 지우기
        removeCartItemsFromCart(cartItems);

        // 7. Order 객체 생성 후 삽입 [아이디 반환]
        Order order = new Order(member, usePoint, newPoint.getEarnedPoint(), createdAt);
        Long orderId = orderDao.createOrder(order, pointId);

        // 8. CartItems 으로 OrderItems 생성 [주문 총가격 checkSum]
        order.addOrderItems(cartItems);
        order.checkTotalPrice(totalPrice);
        List<OrderItem> orderItems = order.getOrderItems();

        // 9. OrderItem 삽입
        for (OrderItem orderItem : orderItems) {
            orderItemDao.createOrderItem(orderId, orderItem);
        }
        return orderId;
    }

    private void usePoint(Member member, Timestamp createdAt, Long usedPoint) {
        List<Point> points = pointDao.findAllAvailablePointsByMemberId(member.getId(), createdAt);
        List<Point> updatePoints = orderPoint.usePoint(usedPoint, points);
        for (Point point : updatePoints) {
            pointDao.updateLeftPoint(point);
        }
    }

    private List<CartItem> findCartItems(Member member, List<Long> cartIds) {
        List<Long> findCartIds = cartItemDao.findAllCartIdsByMemberId(member.getId());
        if (!findCartIds.containsAll(cartIds)) {
            throw new CartItemNotFoundException();
        }

        List<CartItem> cartItems = new ArrayList<>();
        for (Long cartId : cartIds) {
            CartItem cartItem = cartItemDao.findCartItemById(cartId)
                    .orElseThrow(ProductNotFoundException::new);
            cartItem.checkOwner(member);
            cartItem.checkQuantity();
            cartItems.add(cartItem);
        }
        return cartItems;
    }

    private void updateProductStocks(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Long productId = cartItem.getProduct().getId();
            Long stock = cartItem.getProduct().getStock();
            Long quantity = cartItem.getQuantity();
            productDao.updateStock(productId, stock - quantity);
        }
    }

    private void removeCartItemsFromCart(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            cartItemDao.deleteById(cartItem.getId());
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Member member, Long orderId) {
        Order order = orderDao.findOrderById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.checkOwner(member);

        List<OrderItem> orderItems = orderItemDao.findOrderItemsByOrderId(orderId);

        List<OrderItemResponse> orderItemResponses = orderItems
                .stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
        Long totalPrice = orderItems
                .stream()
                .mapToLong(OrderItem::getPrice)
                .sum();

        return new OrderResponse(
                orderId,
                order.getCreatedAt().toString().split(" ")[0],
                orderItemResponses,
                totalPrice,
                order.getUsedPoint(),
                order.getEarnedPoint());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByMember(Member member) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<Order> orders = orderDao.findAllOrdersByMemberId(member.getId());
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemDao.findOrderItemsByOrderId(order.getId());
            List<OrderItemResponse> orderItemResponses = orderItems
                    .stream()
                    .map(OrderItemResponse::of)
                    .collect(Collectors.toList());
            Long totalPrice = orderItems
                    .stream()
                    .mapToLong(OrderItem::getPrice)
                    .sum();
            orderResponses.add(
                    new OrderResponse(
                            order.getId(),
                            order.getCreatedAt().toString().split(" ")[0],
                            orderItemResponses,
                            totalPrice,
                            order.getUsedPoint(),
                            order.getEarnedPoint()
                    ));
        }
        return orderResponses;
    }
}
