package cart.repository;

import static java.util.stream.Collectors.toMap;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.dto.OrderDto;
import cart.dao.dto.OrderProductDto;
import cart.domain.vo.Quantity;
import cart.domain.coupon.Coupon;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;
import cart.exception.OrderNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public OrderRepository(final OrderDao orderDao, final OrderProductDao orderProductDao,
                           final ProductRepository productRepository,
                           final CouponRepository couponRepository, final MemberRepository memberRepository) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
    }

    public Order save(final Order order) {
        final OrderDto orderDto = OrderDto.from(order);
        final Long orderId = orderDao.insert(orderDto);

        final List<OrderProduct> orderProductsAfterSave = saveOrderProducts(orderId, order.getOrderProducts());

        return new Order(orderId, orderProductsAfterSave, order.getTimeStamp(), order.getMember(),
                order.getCoupon().orElse(null));
    }

    private List<OrderProduct> saveOrderProducts(final Long orderId, final List<OrderProduct> orderProducts) {
        final List<OrderProduct> orderProductsAfterSave = new LinkedList<>();
        for (final OrderProduct orderProduct : orderProducts) {
            final OrderProductDto orderProductDto = OrderProductDto.of(orderId, orderProduct);
            final Long insert = orderProductDao.insert(orderProductDto);
            orderProductsAfterSave.add(new OrderProduct(insert, orderProduct.getProduct(),
                    orderProduct.getQuantity()));
        }
        return orderProductsAfterSave;
    }

    public Order findById(final Long id) {
        final OrderDto orderDto = orderDao.findById(id).orElseThrow(OrderNotFoundException::new);
        final List<OrderProduct> orderProducts = findOrderProductsByOrderId(id);
        final Coupon coupon = couponRepository.findById(orderDto.getCouponId());
        return new Order(orderDto.getId(), orderProducts, orderDto.getTimeStamp()
                , memberRepository.findById(orderDto.getMemberId()), coupon);
    }

    private List<OrderProduct> findOrderProductsByOrderId(final Long id) {
        final List<OrderProductDto> orderProductDtos = orderProductDao.findByOrderId(id);
        final Map<Long, Product> products = orderProductDtos.stream()
                .map(OrderProductDto::getProductId)
                .map(productRepository::findById)
                .collect(toMap(Product::getId, Function.identity()));
        return orderProductDtos.stream()
                .map(dto -> new OrderProduct(dto.getId(), products.get(dto.getProductId()),
                        new Quantity(dto.getQuantity())))
                .collect(Collectors.toUnmodifiableList());
    }
}
