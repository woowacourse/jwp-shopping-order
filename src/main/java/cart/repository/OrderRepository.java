package cart.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderedItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderedItemEntity;
import cart.exception.InvalidOrderException;

@Repository
@Transactional(readOnly = true)
public class OrderRepository {
    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;
    private final OrderedItemDao orderedItemDao;
    private final ProductDao productDao;

    public OrderRepository(
            OrderDao orderDao,
            CartItemDao cartItemDao,
            OrderedItemDao orderedItemDao,
            ProductDao productDao
    ) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
        this.orderedItemDao = orderedItemDao;
        this.productDao = productDao;
    }

    @Transactional
    public Long saveOrder(Order order) {
        final OrderEntity orderEntity = new OrderEntity(order.getPrice(), order.getMember().getId());
        final Long orderId = orderDao.addOrder(orderEntity);
        orderedItemDao.saveAll(order.getOrderedItems(), orderId);
        cartItemDao.deleteByIds(order.getOrderedItemIds());
        return orderId;
    }

    public Order findOrderById(Long orderId, Member member) {
        final OrderEntity orderEntity = orderDao.findById(orderId)
                .orElseThrow(() -> new InvalidOrderException("OrderId is not existed; orderId = " + orderId));
        List<OrderedItemEntity> orderedItems = orderedItemDao.findItemsByOrderId(orderId);
        final CartItems cartItems = orderedItemsToCartItems(member, orderedItems);
        return new Order(orderEntity.getId(), orderEntity.getPrice(), member, cartItems);
    }

    private CartItems orderedItemsToCartItems(Member member, List<OrderedItemEntity> orderedItems) {
        final List<Long> productIds = collectIds(orderedItems);
        List<Product> products = productDao.findByIds(productIds);
        final Map<Long, Product> idToProduct = createTable(products);

        final List<CartItem> cartItems = orderedItems.stream()
                .map(orderedItemEntity -> new CartItem(
                        orderedItemEntity.getQuantity(),
                        idToProduct.get(orderedItemEntity.getProductId()),
                        member
                ))
                .collect(Collectors.toUnmodifiableList());
        return CartItems.from(cartItems, member);
    }

    private Map<Long, Product> createTable(List<Product> products) {
        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private List<Long> collectIds(List<OrderedItemEntity> orderedItems) {
        return orderedItems.stream()
                .map(OrderedItemEntity::getProductId)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Order> findOrdersByMember(Member member) {
        final List<OrderEntity> orders = orderDao.findByMember(member);
        List<Order> result = new ArrayList<>();
        for (OrderEntity order : orders) {
            final List<OrderedItemEntity> items = orderedItemDao.findItemsByOrderId(order.getId());
            final CartItems cartItems = orderedItemsToCartItems(member, items);
            result.add(new Order(order.getId(), order.getPrice(), member, cartItems));
        }
        return result;
    }
}
