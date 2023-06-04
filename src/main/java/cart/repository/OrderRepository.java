package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.dao.dto.OrderDto;
import cart.dao.dto.OrderProductDto;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Quantity;
import cart.dto.CouponDto;
import cart.repository.convertor.CouponConvertor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final CartItemDao cartItemDao;
    private final CouponDao couponDao;
    private final ProductDao productDao;

    public OrderRepository(final OrderDao orderDao,
                           final OrderProductDao orderProductDao,
                           final CartItemDao cartItemDao,
                           final CouponDao couponDao,
                           final ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.cartItemDao = cartItemDao;
        this.couponDao = couponDao;
        this.productDao = productDao;
    }

    public Order save(final Order order) {
        OrderDto orderDto = OrderDto.from(order);
        Long orderId = orderDao.insert(orderDto);
        List<OrderProduct> orderProductsAfterSave = saveOrderProducts(orderId, order.getOrderProducts());

        return new Order(
                orderId,
                order.getTimeStamp(),
                order.getMember(),
                order.getOptionalCoupon()
                        .orElse(null),
                orderProductsAfterSave
        );
    }

    private List<OrderProduct> saveOrderProducts(final Long orderId, final List<OrderProduct> orderProducts) {
        List<OrderProduct> orderProductsAfterSave = new LinkedList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductDto orderProductDto = OrderProductDto.of(orderId, orderProduct);
            Long insert = orderProductDao.insert(orderProductDto);
            OrderProduct orderProductHasId = new OrderProduct(insert, orderProduct.getProduct(), orderProduct.getQuantity());
            orderProductsAfterSave.add(orderProductHasId);
        }
        return orderProductsAfterSave;
    }

    public List<CartItem> findCartItemsByIds(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartItemDao::findById)
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersByMember(Member member) {
        List<OrderDto> orderDtos = orderDao.findByMemberId(member.getId());
        return createOrdersFromOrderDtos(member, orderDtos);
    }

    private List<Order> createOrdersFromOrderDtos(final Member member, final List<OrderDto> orderDtos) {
        List<Order> orders = new ArrayList<>();

        for (OrderDto orderDto : orderDtos) {
            Optional<CouponDto> couponDto = couponDao.findById(orderDto.getCouponId());
            Coupon coupon = couponDto.map(CouponConvertor::dtoToDomain)
                    .orElse(null);
            Order order = new Order(orderDto.getId(), orderDto.getTimeStamp(), member, coupon, createOrderProductsFromOrderDto(orderDto));
            orders.add(order);
        }

        return orders;
    }

    private List<OrderProduct> createOrderProductsFromOrderDto(final OrderDto orderDto) {
        return orderProductDao.findByOrderId(orderDto.getId())
                .stream()
                .map(this::createOrderProductFromOrderProductDto)
                .collect(Collectors.toList());
    }

    private OrderProduct createOrderProductFromOrderProductDto(final OrderProductDto orderProductDto) {
        return new OrderProduct(
                orderProductDto.getId(),
                productDao.getProductById(orderProductDto.getProductId()),
                Quantity.from(orderProductDto.getQuantity())
        );
    }

    public Order findOrderById(Member member, Long orderId) {
        OrderDto orderDto = orderDao.findByIdAndMemberId(orderId, member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 고객의 주문이 아닙니다."));
        List<OrderProduct> orderProductsByOrderDto = createOrderProductsFromOrderDto(orderDto);
        return new Order(
                orderId,
                orderDto.getTimeStamp(),
                member,
                findCouponById(orderDto.getCouponId()),
                orderProductsByOrderDto
        );
    }

    private Coupon findCouponById(Long id) {
        Optional<CouponDto> couponDto = couponDao.findById(id);
        return couponDto.map(CouponConvertor::dtoToDomain)
                .orElse(null);
    }

    public void deleteCartItems(List<Long> cartItemIds) {
        for (Long cartItemId : cartItemIds) {
            deleteCartItem(cartItemId);
        }
    }

    private void deleteCartItem(final Long cartItemId) {
        int deleteCount = cartItemDao.deleteById(cartItemId);

        if (deleteCount == 0) {
            throw new IllegalArgumentException("장바구니가 삭제되지 않았습니다.");
        }
    }

}
