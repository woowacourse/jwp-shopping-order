package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final CartItemDao cartItemDao;
    private final CouponDao couponDao;
    private final CouponService couponService;
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final OrderPriceService orderPriceService;

    public OrderService(final CartItemDao cartItemDao, final CouponDao couponDao, final CouponService couponService, final OrderDao orderDao, final ProductDao productDao, final OrderPriceService orderPriceService) {
        this.cartItemDao = cartItemDao;
        this.couponDao = couponDao;
        this.couponService = couponService;
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.orderPriceService = orderPriceService;
    }

    public OrderResponseDto order(final Member member, final OrderRequestDto orderRequestDto) {
        final List<OrderProduct> orderProducts = findOrderProducts(member, orderRequestDto);
        final Order order = new Order(orderProducts, orderPriceService.calculatePriceOf(member, orderProducts, orderRequestDto));
        deleteUsedCoupon(member, orderRequestDto);
        validateExpectPrice(order, orderRequestDto);
        return new OrderResponseDto(completeOrder(member, orderRequestDto, order));
    }

    private List<OrderProduct> findOrderProducts(final Member member, final OrderRequestDto orderRequestDto) {
        final List<CartItem> cartItems = findCartItem(member, orderRequestDto);
        return makeOrderProduct(cartItems, orderRequestDto);
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

    private void deleteUsedCoupon(final Member member, final OrderRequestDto orderRequestDto) {
        if (orderRequestDto.getCouponId() != null) {
            couponDao.deleteUserCoupon(member, orderRequestDto.getCouponId());
        }
    }

    private void validateExpectPrice(final Order order, final OrderRequestDto orderRequestDto) {
        if (order.price() != orderRequestDto.getTotalPrice()) {
            throw new CartItemCalculateException("구매자가 예상한 금액과 실 계산 금액이 다릅니다.");
        }
    }

    private long completeOrder(final Member member, final OrderRequestDto orderRequestDto, final Order order) {
        cartItemDao.deleteByIds(orderRequestDto.getCartItemIds());
        couponService.issueCoupon(member, order);
        return orderDao.save(member, order);
    }

    @Transactional(readOnly = true)
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
        return ProductResponse.of(products.stream()
                .filter(product -> productId == product.getId())
                .findAny()
                .orElseThrow(() -> new IllegalStateException("주문 내역에서 참조하고 있는 상품의 데이터가 유실 되었습니다. " + productId + " 해당 아이디를 가진 상품에 대한 확인을 해주시기 바랍니다.")));
    }

    @Transactional(readOnly = true)
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
