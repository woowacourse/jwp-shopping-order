package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.*;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderProductResponse;
import cart.dto.OrderRequest;
import cart.exception.NoSuchCartItemException;
import cart.exception.NoSuchOrderException;
import cart.exception.PointOverTotalPriceException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<OrderDetailResponse> findOrderByMember(Member member) {
        List<Orders> orders = orderDao.getOrdersByMemberId(member.getId());
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        for (Orders order : orders) {
            List<OrderProductResponse> orderProductResponses = new ArrayList<>();
            for (OrderDetail orderDetail : orderDao.getOrderDetailsByOrdersId(order.getId())) {
                orderProductResponses.add(new OrderProductResponse(orderDetail.getProduct().getId(), orderDetail.getProductName(), orderDetail.getOrderQuantity(), orderDetail.getProductPrice(), orderDetail.getProductImageUrl()));
            }
            int totalPrice = orderProductResponses.stream().map(orderProductResponse -> orderProductResponse.getPrice() * orderProductResponse.getQuantity()).reduce(Integer::sum).orElseThrow(() -> new IllegalArgumentException("총 결제 금액 계산 실패"));
            orderDetailResponses.add(new OrderDetailResponse(order.getId(), order.getCreatedAt(), orderProductResponses, totalPrice, order.getUsedPoint(), order.getEarnedPoint()));
        }

        return orderDetailResponses;
    }

    public OrderDetailResponse findOrderDetailByOrderId(Long id) {
        Orders order = orderDao.getOrdersByOrderId(id).orElseThrow(NoSuchOrderException::new);
        List<OrderProductResponse> orderProductResponses = new ArrayList<>();
        for (OrderDetail orderDetail : orderDao.getOrderDetailsByOrdersId(order.getId())) {
            orderProductResponses.add(new OrderProductResponse(orderDetail.getProduct().getId(), orderDetail.getProductName(), orderDetail.getOrderQuantity(), orderDetail.getProductPrice(), orderDetail.getProductImageUrl()));
        }
        int totalPrice = orderProductResponses.stream().map(orderProductResponse -> orderProductResponse.getPrice() * orderProductResponse.getQuantity()).reduce(Integer::sum).orElseThrow(() -> new IllegalArgumentException("총 결제 금액 계산 실패"));

        return new OrderDetailResponse(order.getId(), order.getCreatedAt(), orderProductResponses, totalPrice, order.getUsedPoint(), order.getEarnedPoint());
    }
}
