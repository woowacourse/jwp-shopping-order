package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderedItem;
import cart.domain.Product;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderResponse;
import cart.dto.OrderedItemResponse;
import cart.dto.ProductResponse;
import cart.exception.PriceValidationException;
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
    private OrderedItemDao orderedItemDao;

    public Long createOrder(Member member, OrderCreateRequest orderCreateRequest) {
        int totalItemPrice = findTotalItemPrice(orderCreateRequest);
        System.out.println("totalItemPrice: " + totalItemPrice);
        int discountedTotalItemPrice = findDiscountedTotalItemPrice(member, orderCreateRequest);
        System.out.println("discountedTotalItemPrice: " + discountedTotalItemPrice);
        int shippingFee = findShippingFee(orderCreateRequest, totalItemPrice);
        System.out.println("shippingFee: " + shippingFee);
        int totalPrice = discountedTotalItemPrice + shippingFee;
        System.out.println("totalPrice: " + totalPrice);
        int totalItemDiscountAmount = findTotalItemDiscountAmount(orderCreateRequest);
        System.out.println("totalItemDiscountAmount: " + totalItemDiscountAmount);
        int totalMemberDiscountAmount = findTotalMemberDiscountAmount(member, orderCreateRequest);
        System.out.println("totalMemberDiscountAmount: " + totalMemberDiscountAmount);

        Order order = new Order(member.getId(), null, totalItemDiscountAmount, totalMemberDiscountAmount, totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice);
        member.order(totalPrice);
        Long orderId = orderDao.createOrder(member.getId(), order);

        //주문한 상품들은 ordered_item에 추가
        List<CartItem> cartItems = findCartItems(orderCreateRequest);
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            OrderedItem orderedItem = new OrderedItem(orderId, product.getName(), product.getPrice(), product.getImage(), cartItem.getQuantity(), product.getIsDiscounted(), product.getDiscountRate());
            orderedItemDao.createOrderedItems(orderId, orderedItem);
        }

        //주문한 상품은 장바구니에서 삭제
        for (CartItem cartItem : cartItems) {
            cartItemDao.delete(member.getId(), cartItem.getId());
        }

        return orderId;
    }

    private int findTotalMemberDiscountAmount(Member member, OrderCreateRequest orderCreateRequest) {
        int totalMemberDiscountAmount = calculateMemberDiscountAmount(member, orderCreateRequest);
        int requestAmount = orderCreateRequest.getTotalMemberDiscountAmount();

        return isSamePrice(totalMemberDiscountAmount, requestAmount);
    }

    private int calculateMemberDiscountAmount(Member member, OrderCreateRequest orderCreateRequest) {
        List<Integer> prices = new ArrayList<>();

        for (Long cartItemId : orderCreateRequest.getCartItemIds()) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
            Product product = cartItem.getProduct();
            int originalPrice = product.getPrice();
            int memberDiscount = member.findDiscountedPercentage();

            if(!product.getIsDiscounted() && memberDiscount > 0){
               int memberDiscountAmount = originalPrice - product.calculateMemberDiscountedPrice(memberDiscount);
                prices.add(memberDiscountAmount);
            }

        }
        return Order.calculatePriceSum(prices);
    }

    private int findTotalItemPrice(OrderCreateRequest orderCreateRequest) {
        int price = calculateTotalItemPrice(orderCreateRequest);
        int requestPrice = orderCreateRequest.getTotalItemPrice();

        return isSamePrice(price, requestPrice);
    }

    public int isSamePrice(int original, int compare){
        if(original == compare){
            return original;
        }
        throw new PriceValidationException();
    }

    private int findDiscountedTotalItemPrice(Member member, OrderCreateRequest orderCreateRequest) {
        int price = calculateDiscountedTotalItemPrice(member, orderCreateRequest);
        int requestPrice = orderCreateRequest.getDiscountedTotalItemPrice();

        return isSamePrice(price, requestPrice);
    }

    private int findShippingFee(OrderCreateRequest orderCreateRequest, int totalItemPrice) {
        int shippingFee = orderCreateRequest.getShippingFee();
        int requestFee = Order.calculateShippingFee(totalItemPrice);

        return isSamePrice(shippingFee, requestFee);
    }

    public List<CartItem> findCartItems(OrderCreateRequest orderCreateRequest) {
        List<CartItem> cartItems = new ArrayList<>();
        for (Long cartItemId : orderCreateRequest.getCartItemIds()) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
            cartItems.add(cartItem);
        }

        return cartItems;
    }

    public int calculateTotalItemPrice(OrderCreateRequest orderCreateRequest) {
        List<Integer> prices = new ArrayList<>();
        for (Long cartItemId : orderCreateRequest.getCartItemIds()) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
            int price = cartItem.getProduct().getPrice();
            prices.add(price);
        }

        return Order.calculatePriceSum(prices);
    }

    public int calculateDiscountedTotalItemPrice(Member member, OrderCreateRequest orderCreateRequest) {
        List<Integer> discountedPrice = new ArrayList<>();

        for (Long cartItemId : orderCreateRequest.getCartItemIds()) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
            Product product = cartItem.getProduct();
            int memberDiscount = member.findDiscountedPercentage();
            int price = product.calculateDiscountedPrice(memberDiscount);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

    private int findTotalItemDiscountAmount(OrderCreateRequest orderCreateRequest) {
        int totalItemDiscountAmount = calculateTotalItemDiscountAmount(orderCreateRequest);
        int requestAmount = orderCreateRequest.getTotalItemDiscountAmount();

        return isSamePrice(totalItemDiscountAmount, requestAmount);
    }

    private int calculateTotalItemDiscountAmount(OrderCreateRequest orderCreateRequest) {
        List<Integer> prices = new ArrayList<>();
        for (Long cartItemId : orderCreateRequest.getCartItemIds()) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
            Product product = cartItem.getProduct();

            if(product.getIsDiscounted()){
                int price = product.calculateProductDiscountAmount();
                prices.add(price);
            }
        }
        return Order.calculatePriceSum(prices);
    }

    public OrderResponse getOrderByIds(Long memberId, Long orderId) {
        Order order = orderDao.findByIds(memberId, orderId);
        List<OrderedItem> orderedItems = orderedItemDao.findByOrderId(order.getId());
        return new OrderResponse(order.getId(), convertToResponse(orderedItems), null, order.getTotalItemPrice(),
                order.getDiscountedTotalItemPrice(), order.getShippingFee(),
                order.getDiscountedTotalItemPrice() + order.getShippingFee());
    }

    public List<OrderedItemResponse> convertToResponse(List<OrderedItem> orderedItems){
        List<OrderedItemResponse> orderedItemResponses = new ArrayList<>();
        for (OrderedItem orderedItem : orderedItems) {
            OrderedItemResponse orderedItemResponse = new OrderedItemResponse(orderedItem.getProductQuantity(),
                    new ProductResponse(orderedItem.getProductName(), orderedItem.getProductPrice(), orderedItem.getProductImage(), orderedItem.getIsDiscounted(), orderedItem.getDiscountedRate()));
            orderedItemResponses.add(orderedItemResponse);
        }

        return orderedItemResponses;
    }

    public OrderResponse getProductById(Long id) {
        Order order = orderDao.findById(id);
        List<OrderedItem> orderedItems = orderedItemDao.findByOrderId(order.getId());
        return new OrderResponse(order.getId(), convertToResponse(orderedItems), null, order.getTotalItemPrice(),
                order.getDiscountedTotalItemPrice(), order.getShippingFee(),
                order.getDiscountedTotalItemPrice() + order.getShippingFee());
    }

    public List<OrderResponse> getAllOrders(Long memberId) {
        List<Order> orders = orderDao.findByMemberId(memberId);

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderedItem> orderedItems = orderedItemDao.findByOrderId(order.getId());
            orderResponses.add(new OrderResponse(order.getId(), convertToResponse(orderedItems), null, order.getTotalItemPrice(),
                    order.getDiscountedTotalItemPrice(), order.getShippingFee(),
                    order.getDiscountedTotalItemPrice() + order.getShippingFee()));
        }
        return orderResponses;
    }
}
