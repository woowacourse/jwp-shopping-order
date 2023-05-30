package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private final OrderDao orderDao;
    @Autowired
    private final CartItemDao cartItemDao;

    public OrderService(OrderDao orderDao, CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
    }

    public Long createOrder(Member member, OrderCreateRequest orderCreateRequest) {
        //TODO: totalPrice구하기 -> List<Int>으로 변경 -> 다 더한 값 return
        int totalPrice = getTotalCount(orderCreateRequest);
        //TODO: shippingFee 구하기 -> totalPrice로 구함
        int shippingFee = getShippingFee(totalPrice);
        //TODO: discountedTotalPrice 구하기 -> member의 등급 확인해서 구함


        new Order(member.getId(), totalPrice, null, )
        Long id = orderDao.createOrder(order);

        return id;
    }

    public int getTotalCount(OrderCreateRequest orderCreateRequest){
        List<OrderItemRequest> orderItemRequests = orderCreateRequest.getOrderItemRequests();
        List<Integer> prices = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            CartItem cartItem = cartItemDao.findById(orderItemRequest.getCartItemId());
            int price = cartItem.getProduct().getPrice();
            prices.add(price);
        }

        return Order.calculateTotalPrice(prices);
    }
}
