package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderedItem;
import cart.domain.Price;
import cart.domain.Product;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private OrderItemDao orderItemDao;

    public Long createOrder(Member member, OrderCreateRequest orderCreateRequest) {
        Long orderId = makeOrder(member, orderCreateRequest);
        List<CartItem> cartItems = createOrderedItems(orderCreateRequest, orderId);
        deleteCartItemsInCart(member, cartItems);

        return orderId;
    }

    private Long makeOrder(Member member, OrderCreateRequest orderCreateRequest) {
        int totalItemPrice = findTotalItemPrice(orderCreateRequest);
        int discountedTotalItemPrice = findDiscountedTotalItemPrice(member, orderCreateRequest);
        int shippingFee = findShippingFee(orderCreateRequest, totalItemPrice);
        int totalPrice = discountedTotalItemPrice + shippingFee;

        return order(member, totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice);
    }

    private Long order(Member member, int totalItemPrice, int discountedTotalItemPrice, int shippingFee, int totalPrice) {
        Order order = new Order(totalItemPrice, discountedTotalItemPrice, shippingFee, member.getId());
        member.createOrder(totalPrice);

        return orderDao.createOrder(member.getId(), order);
    }

    private int findTotalItemPrice(OrderCreateRequest orderCreateRequest) {
        int price = calculateTotalItemPrice(orderCreateRequest);
        int requestPrice = orderCreateRequest.getTotalItemPrice();


        return Price.isSamePrice(price, requestPrice);
    }

    public int calculateTotalItemPrice(OrderCreateRequest orderCreateRequest) {
        List<CartItem> cartItems = getCartItems(orderCreateRequest);

        return Price.calculateTotalItemPrice(cartItems);
    }

    private int findDiscountedTotalItemPrice(Member member, OrderCreateRequest orderCreateRequest) {
        int price = calculateDiscountedTotalItemPrice(member, orderCreateRequest);
        int requestPrice = orderCreateRequest.getDiscountedTotalItemPrice();

        return Price.isSamePrice(price, requestPrice);
    }

    public int calculateDiscountedTotalItemPrice(Member member, OrderCreateRequest orderCreateRequest) {
        List<CartItem> cartItems = getCartItems(orderCreateRequest);
        int memberDiscount = member.findDiscountedPercentage();

        return Price.calculateDiscountedTotalItemPrice(cartItems, memberDiscount);
    }

    private int findShippingFee(OrderCreateRequest orderCreateRequest, int totalItemPrice) {
        int shippingFee = Price.calculateShippingFee(totalItemPrice);
        int requestFee = orderCreateRequest.getShippingFee();

        return Price.isSamePrice(shippingFee, requestFee);
    }

    private void deleteCartItemsInCart(Member member, List<CartItem> cartItems) {
        cartItems.forEach(cartItem -> cartItemDao.delete(member.getId(), cartItem.getId()));
    }


    private List<CartItem> createOrderedItems(OrderCreateRequest orderCreateRequest, Long orderId) {
        List<CartItem> cartItems = getCartItems(orderCreateRequest);
        cartItems.forEach(cartItem -> {
            Product product = cartItem.getProduct();
            OrderedItem orderedItem = new OrderedItem(orderId, product.getName(), product.getPrice(), product.getImageUrl(), cartItem.getQuantity(), product.getDiscountRate());
            orderItemDao.createOrderedItems(orderId, orderedItem);
        });
        return cartItems;
    }

    public List<CartItem> getCartItems(OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest.getCartItemIds()
                .stream()
                .map(cartItemId -> cartItemDao.findById(cartItemId))
                .collect(Collectors.toList());
    }


    public OrderResponse getOrderByIds(Long memberId, Long orderId) {
        Order order = orderDao.findByIds(memberId, orderId);
        List<OrderedItem> orderedItems = orderItemDao.findByOrderId(order.getId());

        return createOrderResponse(order, orderedItems);
    }

    private OrderResponse createOrderResponse(Order order, List<OrderedItem> orderedItems) {
        int totalItemDiscountedAmount = calculateTotalItemDiscountedPriceForOrder(orderedItems);
        int totalMemberDiscountAmount = calculateTotalMemberDiscountAmountForOrder(order, totalItemDiscountedAmount);

        return OrderResponse.of(order, orderedItems, totalItemDiscountedAmount, totalMemberDiscountAmount);
    }

    private int calculateTotalItemDiscountedPriceForOrder(List<OrderedItem> orderedItems) {
        List<Integer> itemDiscountedPrice = new ArrayList<>();
        for (OrderedItem orderedItem : orderedItems) {
            calculateItemDiscountedPriceForOrder(itemDiscountedPrice, orderedItem);
        }
        return Order.calculatePriceSum(itemDiscountedPrice);
    }

    private void calculateItemDiscountedPriceForOrder(List<Integer> itemDiscountedPrice, OrderedItem orderedItem) {
        for (int i = 0; i < orderedItem.getQuantity(); i++) {
            int discountedPrice = calculateDiscountedPrice(orderedItem);
            itemDiscountedPrice.add(discountedPrice);
        }
    }

    public int calculateDiscountedPrice(OrderedItem orderedItem) {
        if (orderedItem.getDiscountRate() > 0) {
            return orderedItem.getPrice() - ((orderedItem.getDiscountRate() * orderedItem.getPrice() / 100 - orderedItem.getPrice()) * -1);
        }
        return 0;
    }


    private int calculateTotalMemberDiscountAmountForOrder(Order order, int totalItemDiscountedAmount) {
        return order.getTotalItemPrice() - order.getDiscountedTotalItemPrice() - totalItemDiscountedAmount;
    }

    public List<OrderResponse> getAllOrders(Long memberId) {
        List<Order> orders = orderDao.findAllByMemberId(memberId);

        List<OrderResponse> orderResponses = new ArrayList<>();
        orders.forEach(order -> {
            List<OrderedItem> orderedItems = orderItemDao.findByOrderId(order.getId());
            orderResponses.add(createOrderResponse(order, orderedItems));
        });

        return orderResponses;
    }
}
