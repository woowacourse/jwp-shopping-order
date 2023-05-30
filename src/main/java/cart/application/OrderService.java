package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private MemberDao memberDao;

    public OrderService(OrderDao orderDao, CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
    }

    public Long createOrder(Member member, OrderCreateRequest orderCreateRequest) {
        //TODO: totalItemPrice구하기 -> List<Int>으로 변경 -> 다 더한 값 return
        int totalItemPrice = findTotalItemPrice(orderCreateRequest);
        //TODO: shippingFee 구하기 -> totalPrice로 구함
        int shippingFee = Order.calculateShippingFee(totalItemPrice);
        //TODO: discountedTotalPrice 구하기 -> 상품 할인 체크 -> 상품 할인이 있다면 상품 할인 적용, 아니라면 member의 등급 확인해서 구함
        int discountedTotalPrice = findDiscountedTotalItemPrice(member, orderCreateRequest, totalItemPrice);

        Order order = new Order(member.getId(), totalItemPrice + shippingFee, totalItemPrice, null, shippingFee, discountedTotalPrice);
        Long id = orderDao.createOrder(member.getId(), order);

        return id;
    }

    public int findTotalItemPrice(OrderCreateRequest orderCreateRequest){
        List<OrderItemRequest> orderItemRequests = orderCreateRequest.getOrderItemRequests();
        List<Integer> prices = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            CartItem cartItem = cartItemDao.findById(orderItemRequest.getCartItemId());
            int price = cartItem.getProduct().getPrice();
            prices.add(price);
        }

        return Order.calculatePriceSum(prices);
    }

    public int findDiscountedTotalItemPrice(Member member, OrderCreateRequest orderCreateRequest, int totalItemPrice){
        List<OrderItemRequest> orderItemRequests = orderCreateRequest.getOrderItemRequests();
        List<Integer> discountedPrice = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            CartItem cartItem = cartItemDao.findById(orderItemRequest.getCartItemId());
            Product product = cartItem.getProduct();
            int discountedPercentage = member.calculateDiscountedPercentage();

            int price = product.calculateDiscountedPrice(discountedPercentage);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }
}
