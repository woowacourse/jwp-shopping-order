package cart.service;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.OrderEntity;
import cart.domain.OrderItemEntity;
import cart.domain.Point;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.dto.request.CartItemInfoRequest;
import cart.dto.request.OrderRequest;
import cart.dto.request.ProductInfoRequest;
import cart.dto.response.OrderItemResponse;
import cart.dto.response.OrderResponse;
import cart.exception.OrderException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.util.CurrentTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class OrderService {

    private final MemberRepository memberRepository;
    private final ProductDao productDao;
    private final CartItemRepository cartItemRepository;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderService(MemberRepository memberRepository, ProductDao productDao, CartItemRepository cartItemRepository, OrderDao orderDao, OrderItemDao orderItemDao) {
        this.memberRepository = memberRepository;
        this.productDao = productDao;
        this.cartItemRepository = cartItemRepository;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public long order(final Member member, final OrderRequest orderRequest) {
        List<CartItemInfoRequest> cartItemInfos = orderRequest.getCartItems();
        CartItems cartItems = toCartItems(cartItemInfos);

        cartItems.validateOwner(member);
        cartItems.validateTotalProductPrice(orderRequest.getTotalProductPrice());
        validateRequestTotalPrice(orderRequest);

        removeCartItem(cartItems);
        updatePoint(member, orderRequest.getTotalProductPrice(), orderRequest.getUsePoint());
        updateProductStock(cartItems);
        return saveOrderData(member, orderRequest);
    }

    private CartItems toCartItems(final List<CartItemInfoRequest> cartItemInfos) {
        List<CartItem> cartItems = cartItemInfos.stream()
                .map(cartItemInfo -> cartItemRepository.findById(cartItemInfo.getCartItemId()))
                .collect(toList());

        return new CartItems(cartItems);
    }

    private void validateRequestTotalPrice(final OrderRequest orderRequest) {
        int totalProductPrice = orderRequest.getTotalProductPrice();
        int totalDeliveryFee = orderRequest.getTotalDeliveryFee();
        int usePoint = orderRequest.getUsePoint();
        int totalPrice = orderRequest.getTotalPrice();

        if (totalPrice != (totalProductPrice + totalDeliveryFee - usePoint)) {
            throw new OrderException.MismatchedTotalPrice("요청에서 계산된 결과와 서버에서 계산된 결과가 일치하지 않습니다.");
        }
    }

    private void removeCartItem(final CartItems cartItems) {
        for (final CartItem cartItem : cartItems.getCartItems()) {
            cartItemRepository.deleteById(cartItem.getId());
        }
    }

    private void updatePoint(final Member member, final int totalProductPrice, final int usePoint) {
        Member newMember = member.savePoint(totalProductPrice);
        Member result = newMember.usePoint(new Point(usePoint));

        memberRepository.update(result);
    }

    private void updateProductStock(final CartItems cartItems) {
        for (final CartItem cartItem : cartItems.getCartItems()) {
            Product product = cartItem.getProduct();
            Quantity quantity = cartItem.getQuantity();

            productDao.update(product.updateStock(quantity));
        }
    }

    private long saveOrderData(final Member member, final OrderRequest orderRequest) {
        OrderEntity orderEntity = toOrderEntity(member, orderRequest);
        long orderId = orderDao.insert(orderEntity);

        saveOrderItems(orderId, orderRequest);

        return orderId;
    }

    private OrderEntity toOrderEntity(final Member member, final OrderRequest orderRequest) {
        return new OrderEntity(
                CurrentTimeUtil.asString(),
                member.getId(),
                orderRequest.getTotalProductPrice(),
                orderRequest.getTotalDeliveryFee(),
                orderRequest.getUsePoint(),
                orderRequest.getTotalPrice()
        );
    }

    private void saveOrderItems(final long orderId, final OrderRequest orderRequest) {
        for (final CartItemInfoRequest cartItemInfo : orderRequest.getCartItems()) {
            ProductInfoRequest productInfo = cartItemInfo.getProduct();

            OrderItemEntity orderItemEntity = toOrderItemEntity(orderId, productInfo, cartItemInfo.getQuantity());
            orderItemDao.insert(orderItemEntity);
        }
    }

    private OrderItemEntity toOrderItemEntity(final long orderId, final ProductInfoRequest productInfo, final int quantity) {
        return new OrderItemEntity(
                orderId,
                productInfo.getProductId(),
                productInfo.getName(),
                productInfo.getPrice(),
                productInfo.getImageUrl(),
                quantity
        );
    }

    public List<OrderResponse> getAllOrderBy(final Long memberId) {
        List<OrderResponse> result = new ArrayList<>();

        for (final OrderEntity orderEntity : orderDao.findAllByMemberId(memberId)) {
            List<OrderItemEntity> twoOrderItemEntities = orderItemDao.findTwoByOrderId(orderEntity.getId());

            List<OrderItemResponse> orderItemResponses = twoOrderItemEntities.stream()
                    .map(orderItemEntity -> OrderItemResponse.of(orderItemEntity, productDao.findById(orderItemEntity.getProductId())))
                    .collect(toList());

            result.add(OrderResponse.of(orderEntity, orderItemResponses));
        }

        return result;
    }

    public OrderResponse findOrderBy(final Long orderId) {
        OrderEntity orderEntity = orderDao.findById(orderId);
        List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderEntity.getId());

        List<OrderItemResponse> orderItemResponses = orderItemEntities.stream()
                .map(orderItemEntity -> OrderItemResponse.of(orderItemEntity, productDao.findById(orderItemEntity.getProductId())))
                .collect(toList());

        return OrderResponse.of(orderEntity, orderItemResponses);
    }
}
