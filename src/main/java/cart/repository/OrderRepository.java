package cart.repository;

import static java.util.stream.Collectors.toMap;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.dao.dto.OrderDto;
import cart.dao.dto.OrderProductDto;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import cart.domain.Quantity;
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
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public OrderRepository(final OrderDao orderDao, final OrderProductDao orderProductDao, final ProductDao productDao,
                           final MemberDao memberDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    public Order save(final Order order) {
        final OrderDto orderDto = OrderDto.from(order);
        final Long orderId = orderDao.insert(orderDto);

        final List<OrderProduct> orderProductsAfterSave = saveOrderProducts(orderId, order.getOrderProducts());

        return new Order(orderId, order.getTimeStamp(), order.getMember(), orderProductsAfterSave);
    }

    private List<OrderProduct> saveOrderProducts(final Long orderId, final List<OrderProduct> orderProducts) {
        List<OrderProduct> orderProductsAfterSave = new LinkedList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductDto orderProductDto = OrderProductDto.of(orderId, orderProduct);
            Long insert = orderProductDao.insert(orderProductDto);
            orderProductsAfterSave.add(new OrderProduct(insert, orderProduct.getProduct(),
                    orderProduct.getQuantity()));
        }
        return orderProductsAfterSave;
    }

    public Order findById(final Long id) {
        final OrderDto orderDto = orderDao.findById(id).orElseThrow(OrderNotFoundException::new);
        final List<OrderProduct> orderProducts = findOrderProductsByOrderId(id);
        return new Order(orderDto.getId(), orderDto.getTimeStamp()
                , memberDao.getMemberById(orderDto.getMemberId()), orderProducts);
    }

    private List<OrderProduct> findOrderProductsByOrderId(final Long id) {
        final List<OrderProductDto> orderProductDtos = orderProductDao.findByOrderId(id);
        final Map<Long, Product> products = orderProductDtos.stream()
                .map(OrderProductDto::getProductId)
                .map(productDao::getProductById)
                .collect(toMap(Product::getId, Function.identity()));
        return orderProductDtos.stream()
                .map(dto -> new OrderProduct(dto.getId(), products.get(dto.getProductId()),
                        new Quantity(dto.getQuantity())))
                .collect(Collectors.toUnmodifiableList());
    }
}
