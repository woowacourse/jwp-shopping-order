package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.MemberDao;
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
import javax.swing.text.html.Option;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;
    private final CouponDao couponDao;
    private final ProductDao productDao;

    public OrderRepository(final OrderDao orderDao,
                           final OrderProductDao orderProductDao,
                           final CartItemDao cartItemDao,
                           final MemberDao memberDao,
                           final CouponDao couponDao,
                           final ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
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
                order.getCoupon(),
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

    public List<CartItem> findCartItemByIds(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartItemDao::findById)
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersByMember(Member member) {
        Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        List<OrderDto> orderDtos = orderDao.findByMemberId(memberByEmail.getId());
        return getOrdersByOrderDtos(memberByEmail, orderDtos);
    }

    public Order findOrderById(Member member, Long orderId) {
        Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        OrderDto orderDto = orderDao.findByIdAndMemberId(orderId, memberByEmail.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 고객의 주문이 아닙니다."));
        List<OrderProduct> orderProductsByOrderDto = getOrderProductsByOrderDto(orderDto);
        return new Order(
                orderId,
                orderDto.getTimeStamp(),
                memberByEmail,
                findCouponById(orderDto.getCouponId()),
                orderProductsByOrderDto
        );
    }

    private List<Order> getOrdersByOrderDtos(final Member memberByEmail, final List<OrderDto> orderDtos) {
        List<Order> orders = new ArrayList<>();

        for (OrderDto orderDto : orderDtos) {
            Optional<CouponDto> couponDto = couponDao.findById(orderDto.getCouponId());
            Optional<Coupon> coupon = couponDto.map(CouponConvertor::dtoToDomain);
            Order order = new Order(orderDto.getId(), orderDto.getTimeStamp(), memberByEmail, coupon, getOrderProductsByOrderDto(orderDto));
            orders.add(order);
        }

        return orders;
    }

    private Optional<Coupon> findCouponById(Long id) {
        Optional<CouponDto> couponDto = couponDao.findById(id);
        return couponDto.map(CouponConvertor::dtoToDomain);
    }

    private List<OrderProduct> getOrderProductsByOrderDto(final OrderDto orderDto) {
        return orderProductDao.findByOrderId(orderDto.getId())
                .stream()
                .map(orderProductDto -> new OrderProduct(orderProductDto.getId(),
                        productDao.getProductById(orderProductDto.getProductId()),
                        Quantity.from(orderProductDto.getQuantity())))
                .collect(Collectors.toList());
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
