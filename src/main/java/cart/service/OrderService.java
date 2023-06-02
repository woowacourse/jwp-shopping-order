package cart.service;

import cart.dao.*;
import cart.domain.*;
import cart.dto.*;
import cart.exception.InvalidCardException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Long totalOrderPrice = 0L;
        Order order = new Order(member, LocalDateTime.now(), new Point(orderRequest.getPoint()));
        Long orderId = shoppingOrderDao.save(order);
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
            cartItem.checkOwner(member);
            Integer quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();
            totalOrderPrice += quantity * product.getPrice().getValue();
            orderedItemDao.save(product.getId(), orderId, quantity);
            cartItemDao.deleteById(cartItemId);
        }
        Point currentPoint = member.getPoint();
        Point savingPoint = PointEarningPolicy.calculateSavingPoints(totalOrderPrice - orderRequest.getPoint());
        Point updated = new Point(currentPoint.getValue() - orderRequest.getPoint() + savingPoint.getValue());
        memberDao.updatePoints(updated.getValue(), member);

        return orderId;
    }

    public List<OrderResponse> findAll(Long id) {
        List<OrderResponseEntity> orderResponseEntities = shoppingOrderDao.findAll(id);
        return getOrderResponse(orderResponseEntities);
    }

    private List<OrderResponse> getOrderResponse(List<OrderResponseEntity> orderResponseEntities) {
        Map<Long, OrderResponse> orderMap = new HashMap<>();
        for (OrderResponseEntity entity : orderResponseEntities) {
            Long orderId = entity.getOrderId();
            Long usedPoint = entity.getUsedPoint();
            LocalDateTime orderedAt = entity.getOrderedAt();
            orderMap.put(orderId, new OrderResponse(orderId, usedPoint, null, orderedAt, new ArrayList<>()));
        }

        for (OrderResponseEntity entity : orderResponseEntities) {
            Long orderId = entity.getOrderId();
            Long orderItemId = entity.getOrderItemId();
            Integer quantity = entity.getQuantity();
            Long productId = entity.getProductId();
            String productName = entity.getProductName();
            Integer productPrice = entity.getProductPrice();
            String productImageUrl = entity.getProductImageUrl();

            ProductResponse productResponse = new ProductResponse(productId, productName, productPrice, productImageUrl);

            OrderItemResponse orderItemResponse = new OrderItemResponse(orderItemId, quantity, productResponse);

            orderMap.get(orderId).getProducts().add(orderItemResponse);
        }

        for (OrderResponse response : orderMap.values()) {
            long totalPrice = response.getProducts().stream()
                    .mapToLong(detailOrder -> detailOrder.getQuantity() * detailOrder.getProductResponse().getPrice())
                    .sum();
            long usedPoint = response.getUsedPoint();
            Point savingPoint = PointEarningPolicy.calculateSavingPoints(totalPrice - usedPoint);
            response.setSavedPoint(savingPoint.getValue());
        }
        return new ArrayList<>(orderMap.values());
    }

    public OrderResponse findById(Member member, Long id) {
        Member orderOwner = memberDao.findByOrderId(id);
        if (!orderOwner.equals(member)) {
            throw new IllegalArgumentException("로그인한 사용자의 주문 목록이 아닙니다");
        }

        List<OrderResponseEntity> detailOrders = shoppingOrderDao.findById(id);
        long totalPrice = detailOrders.stream()
                .mapToLong(detailOrder -> detailOrder.getQuantity() * detailOrder.getProductPrice())
                .sum();
        long usedPoint = detailOrders.get(0).getUsedPoint();
        Point savingPoint = PointEarningPolicy.calculateSavingPoints(totalPrice - usedPoint);
        long orderId = detailOrders.get(0).getOrderId();
        LocalDateTime orderedAt = detailOrders.get(0).getOrderedAt();

        List<OrderItemResponse> orderItems = new ArrayList<>();

        for (OrderResponseEntity detailOrder : detailOrders) {
            Long orderItemId = detailOrder.getOrderItemId();
            Integer quantity = detailOrder.getQuantity();
            Long productId = detailOrder.getProductId();
            String productName = detailOrder.getProductName();
            Integer productPrice = detailOrder.getProductPrice();
            String productImageUrl = detailOrder.getProductImageUrl();

            OrderItemResponse orderItem = new OrderItemResponse(orderItemId, quantity, new ProductResponse(productId, productName, productPrice, productImageUrl));
            orderItems.add(orderItem);
        }
        return new OrderResponse(orderId, usedPoint, savingPoint.getValue(), orderedAt, orderItems);
    }
}
