package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.DeliveryFeeCalculator;
import cart.domain.DiscountCalculator;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.OrderAddRequest;
import cart.dto.OrderItemRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    private final DeliveryFeeCalculator deliveryFeeCalculator;
    private final DiscountCalculator discountCalculator;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final OrderItemDao orderItemDao;
    private final OrderDao orderDao;
    
    public OrderService(DeliveryFeeCalculator deliveryFeeCalculator, DiscountCalculator discountCalculator,
            CartItemDao cartItemDao, ProductDao productDao, OrderItemDao orderItemDao, OrderDao orderDao) {
        this.deliveryFeeCalculator = deliveryFeeCalculator;
        this.discountCalculator = discountCalculator;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
    }
    
    public Long addOrder(Member member, OrderAddRequest request) {
        Cart cart = new Cart(cartItemDao.findByMemberId(member.getId()));
        List<OrderItem> itemsToOrder = cart.createOrderItems(findRequestCartItems(member, request.getOrderItemRequestList()));
        Order order = new Order(null,
                member.getId(),
                itemsToOrder,
                deliveryFeeCalculator.calculate(member, itemsToOrder),
                discountCalculator.calculate(member, itemsToOrder),
                request.getCreatedAt()
                );
        Long orderId = orderDao.save(order);
        orderItemDao.save(orderId, order.getOrderItems());
        cartItemDao.deleteAll(member.getId(), cart.getCartItems());
        return orderId;
    }
    
    private List<CartItem> findRequestCartItems(Member member, List<OrderItemRequest> requests) {
        List<CartItem> itemsToOrder = new ArrayList<>();
        for (OrderItemRequest request : requests) {
            Product product = productDao.getProductById(request.getProduct_id());
            int quantity = request.getQuantity();
            itemsToOrder.add(new CartItem(null, quantity, product, member));
        }
        return itemsToOrder;
    }
    
    public List<Order> findOrdersByMember(Member member) {
        return orderDao.findOrders(member.getId());
    }
    
    public Order findOrderById(Member member, Long orderId) {
        return orderDao.findOrder(member.getId(), orderId);
    }
    
    //todo : addOrder메서드에서  productDao.getProductById()메서드에서 id없는 경우 Optional설정해줘야함?
    // 그게 아니면, 예외 처리 어떻게 해야하는지 생각해보기!
}
