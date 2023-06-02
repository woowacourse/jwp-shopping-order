package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.dto.request.OrderRequestDto;
import cart.dto.response.OrderDetail;
import cart.dto.response.OrderInfo;
import cart.dto.response.OrderProductInfo;
import cart.dto.response.OrderResponseDto;
import cart.dto.response.ProductResponse;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.exception.CartItemCalculateException;
import cart.exception.CartItemNotFoundException;
import cart.exception.CouponNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CartItemDao cartItemDao;
    private final CouponDao couponDao;
    private final OrderDao orderDao;
    private final ProductDao productDao;

    public OrderService(final CartItemDao cartItemDao, final CouponDao couponDao, final OrderDao orderDao, final ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.couponDao = couponDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    @Transactional
    public OrderResponseDto order(final Member member, final OrderRequestDto orderRequestDto) {
        final List<CartItem> cartItems = findCartItem(member, orderRequestDto);

        final Order order = new Order(makeOrderProduct(cartItems, orderRequestDto));
        order.applyDeliveryFee(new DeliveryFee(3000));
        applyCoupon(member, orderRequestDto, order);
        validateExpectPrice(order, orderRequestDto);
        return new OrderResponseDto(completeOrder(member, orderRequestDto, order));
    }

    private List<CartItem> findCartItem(final Member member, final OrderRequestDto orderRequestDto) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        checkCartItem(cartItems, orderRequestDto);
        return cartItems;
    }

    private void checkCartItem(final List<CartItem> cartItems, final OrderRequestDto orderRequestDto) {
        final List<Long> cartItemsId = cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
        final boolean allItemExist = cartItemsId.containsAll(orderRequestDto.getCartItemIds());
        if (!allItemExist) {
            throw new CartItemNotFoundException("주문 요청에 존재하지 않는 카트 아이템이 포함되어 있습니다.");
        }
    }

    private List<OrderProduct> makeOrderProduct(final List<CartItem> cartItems, final OrderRequestDto orderRequestDto) {
        final List<Long> cartItemsId = orderRequestDto.getCartItemIds();
        return cartItems.stream()
                .filter(cartItem -> cartItemsId.contains(cartItem.getId()))
                .map(OrderProduct::of)
                .collect(Collectors.toList());
    }

    private void applyCoupon(final Member member, final OrderRequestDto orderRequestDto, final Order order) {
        if (Objects.isNull(orderRequestDto.getCouponId())) {
            return;
        }
        order.applyCoupon(findCoupon(member, orderRequestDto));
        couponDao.deleteUserCoupon(member, orderRequestDto.getCouponId());
    }

    private Coupon findCoupon(final Member member, final OrderRequestDto orderRequestDto) {
        final List<Coupon> couponById = couponDao.findCouponById(member.getId());
        final Optional<Coupon> requestCoupon = couponById.stream().filter(coupon -> coupon.getId() == orderRequestDto.getCouponId()).findAny();
        if (requestCoupon.isEmpty()) {
            throw new CouponNotFoundException("해당 쿠폰을 유저가 가지고 있지 않습니다,");
        }
        return requestCoupon.get();
    }

    private void validateExpectPrice(final Order order, final OrderRequestDto orderRequestDto) {
        if (order.price() != orderRequestDto.getTotalPrice()) {
            throw new CartItemCalculateException("구매자가 예상한 금액과 실 계산 금액이 다릅니다.");
        }
    }

    private long completeOrder(final Member member, final OrderRequestDto orderRequestDto, final Order order) {
        cartItemDao.deleteByIds(orderRequestDto.getCartItemIds());
        return orderDao.save(member, order);
    }

    public List<OrderInfo> findOrderOf(final Member member) {
        final List<OrderEntity> orderByMemberId = orderDao.findOrderByMemberId(member.getId());
        final List<OrderItemEntity> orderProductByOrderByIds = orderDao.findOrderProductByIds(
                orderByMemberId.stream()
                        .map(OrderEntity::getId)
                        .collect(Collectors.toList()));
        final List<Product> productByIds = productDao.getProductByIds(
                orderProductByOrderByIds.stream()
                        .map(OrderItemEntity::getProductId)
                        .collect(Collectors.toList()));
        return makeOrderInfo(orderByMemberId, orderProductByOrderByIds, productByIds);
    }

    private List<OrderInfo> makeOrderInfo(final List<OrderEntity> orderEntities, final List<OrderItemEntity> orderItemEntities, final List<Product> products) {
        return orderEntities.stream()
                .map(orderEntity -> new OrderInfo(orderEntity.getId(), orderProductInfoByOrderId(orderEntity.getId(), orderItemEntities, products)))
                .collect(Collectors.toList());
    }

    private List<OrderProductInfo> orderProductInfoByOrderId(final long orderId, final List<OrderItemEntity> orderItemEntities, final List<Product> products) {
        return orderItemEntities.stream()
                .filter(orderItemEntity -> orderItemEntity.getOrderId() == orderId)
                .map(orderItemEntity -> new OrderProductInfo(orderItemEntity.getId(), orderItemEntity.getQuantity(), productResponseOf(orderItemEntity.getProductId(), products)))
                .collect(Collectors.toList());
    }

    private ProductResponse productResponseOf(final long productId, final List<Product> products) {
        try {
            return ProductResponse.of(products.stream()
                    .filter(product -> productId == product.getId())
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("주문 목록들의 상품 정보를 만드는 과정 중에 장바구니에 존재하는 상품의 Id를 가진 실제 상품 데이터를 읽어오지 못했습니다.")));
        } catch (final IllegalStateException e) {
            return ProductResponse.deleted();
        }
    }

    public OrderDetail findOrderDetail(final Member member, final long orderId) {
        final OrderEntity orderEntity = orderDao.findById(orderId);
        orderEntity.checkOwner(member);
        final List<OrderItemEntity> orderItemEntities = orderDao.findOrderProductByOrderId(orderId);
        final List<Product> productByIds = productDao.getProductByIds(orderItemEntities.stream()
                .map(OrderItemEntity::getProductId)
                .collect(Collectors.toList()));
        return new OrderDetail(new OrderInfo(orderId, orderProductInfoByOrderId(orderId, orderItemEntities, productByIds)), orderEntity.getPrice());
    }

}
