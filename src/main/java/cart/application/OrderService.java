package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.*;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.exception.NoSuchCartItemException;
import cart.exception.NoSuchOrderException;
import cart.exception.PointOverTotalPriceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class OrderService {

    private final OrderDao orderDao;
    private final PointService pointService;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public OrderService(OrderDao orderDao, PointService pointService, CartItemDao cartItemDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.pointService = pointService;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public Long createOrder(Member member, OrderRequest orderRequest) {
        LocalDateTime now = LocalDateTime.now();

        if (orderRequest.getTotalPrice() < orderRequest.getPoint()) {
            throw new PointOverTotalPriceException();
        }
        pointService.usePointByMember(member, orderRequest.getPoint());
        Point point = pointService.savePointByMember(member, orderRequest.getTotalPrice() - orderRequest.getPoint(), now);
        Orders orders = new Orders(member, point, point.getEarnedPoint(), orderRequest.getPoint(), now);
        Long id = orderDao.createOrders(orders);
        Orders createdOrders = new Orders(id, orders.getMember(), orders.getPoint(), orders.getEarnedPoint(), orders.getUsedPoint(), orders.getCreatedAt());
        int productSum = 0;
        for (Long cartId : orderRequest.getCartIds()) {
            CartItem cartItem = cartItemDao.findById(cartId).orElseThrow(NoSuchCartItemException::new);
            cartItem.checkOwner(member);
            Product product = cartItem.getProduct();
            productDao.updateProduct(product.getId(), new Product(product.getName(), product.getPrice(), product.getImageUrl(), product.getStock() - cartItem.getQuantity()));
            OrderDetail orderDetail = new OrderDetail(createdOrders, product, product.getName(), product.getPrice(), product.getImageUrl(), cartItem.getQuantity());
            productSum += product.getPrice() * cartItem.getQuantity();
            orderDao.createOrderDetail(orderDetail);
            cartItemDao.deleteById(cartId);
        }
        if (productSum != orderRequest.getTotalPrice()) {
            throw new IllegalArgumentException("총계가 결제 예정 금액과 다릅니다.");
        }

        return createdOrders.getId();
    }

    @Transactional(readOnly = true)
    public List<OrderDetailResponse> findOrderByMember(Member member) {
        List<Orders> orders = orderDao.getOrdersByMemberId(member.getId());
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        for (Orders ordersOne : orders) {
            orderDetailResponses.add(generateOrderDetailResponse(ordersOne));
        }

        return orderDetailResponses;
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderDetailByOrderId(Long id) {
        Orders orders = orderDao.getOrdersByOrderId(id).orElseThrow(NoSuchOrderException::new);
        return generateOrderDetailResponse(orders);
    }

    private OrderDetailResponse generateOrderDetailResponse(Orders orders) {
        List<OrderDetail> orderDetails = orderDao.getOrderDetailsByOrdersId(orders.getId());

        return OrderDetailResponse.of(orders, orderDetails);
    }
}
