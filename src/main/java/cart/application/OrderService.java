package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderedItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderedItem;
import cart.domain.Price;
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
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private OrderedItemDao orderedItemDao;

    public Long createOrder(Member member, OrderCreateRequest orderCreateRequest) {
        Long orderId = makeOrder(member, orderCreateRequest);

        //주문한 상품들은 ordered_item에 추가
        List<CartItem> cartItems = createOrderedItems(orderCreateRequest, orderId);

        //주문한 상품은 장바구니에서 삭제
        deleteCartItemsInCart(member, cartItems);

        return orderId;
    }

    private Long makeOrder(Member member, OrderCreateRequest orderCreateRequest) {
        int totalItemPrice = findTotalItemPrice(orderCreateRequest);
        int discountedTotalItemPrice = findDiscountedTotalItemPrice(member, orderCreateRequest);
        int shippingFee = findShippingFee(orderCreateRequest, totalItemPrice);
        int totalPrice = discountedTotalItemPrice + shippingFee;
        int totalItemDiscountAmount = findTotalItemDiscountAmount(orderCreateRequest);
        int totalMemberDiscountAmount = findTotalMemberDiscountAmount(member, orderCreateRequest);

        Order order = new Order(member.getId(), null, totalItemDiscountAmount, totalMemberDiscountAmount, totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice);
        member.createOrder(totalPrice);
        Long orderId = orderDao.createOrder(member.getId(), order);
        return orderId;
    }

    private void deleteCartItemsInCart(Member member, List<CartItem> cartItems) {
        cartItems.forEach(cartItem -> cartItemDao.delete(member.getId(), cartItem.getId()));
    }

    private List<CartItem> createOrderedItems(OrderCreateRequest orderCreateRequest, Long orderId) {
        List<CartItem> cartItems = getCartItems(orderCreateRequest);
        cartItems.forEach(cartItem -> {
            Product product = cartItem.getProduct();
            OrderedItem orderedItem = new OrderedItem(orderId, product.getName(), product.getPrice(), product.getImageUrl(), cartItem.getQuantity(), product.getIsDiscounted(), product.getDiscountRate());
            orderedItemDao.createOrderedItems(orderId, orderedItem);
        });
        return cartItems;
    }

    private int findTotalMemberDiscountAmount(Member member, OrderCreateRequest orderCreateRequest) {
        int totalMemberDiscountAmount = calculateMemberDiscountAmount(member, orderCreateRequest);
        int requestAmount = orderCreateRequest.getTotalMemberDiscountAmount();

        return isSamePrice(totalMemberDiscountAmount, requestAmount);
    }

    private int calculateMemberDiscountAmount(Member member, OrderCreateRequest orderCreateRequest) {
        List<CartItem> cartItems = getCartItems(orderCreateRequest);
        int memberDiscount = member.findDiscountedPercentage();

        return Price.calculateTotalMemberDiscountAmount(cartItems, memberDiscount);
    }

    private int findTotalItemPrice(OrderCreateRequest orderCreateRequest) {
        int price = calculateTotalItemPrice(orderCreateRequest);
        int requestPrice = orderCreateRequest.getTotalItemPrice();

        return isSamePrice(price, requestPrice);
    }

    public int isSamePrice(int original, int compare) {
        if (original == compare) {
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
        int shippingFee = Price.calculateShippingFee(totalItemPrice);
        int requestFee = orderCreateRequest.getShippingFee();

        return isSamePrice(shippingFee, requestFee);
    }

    public List<CartItem> getCartItems(OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest.getCartItemIds()
                .stream()
                .map(cartItemId -> cartItemDao.findById(cartItemId))
                .collect(Collectors.toList());
    }

    public int calculateTotalItemPrice(OrderCreateRequest orderCreateRequest) {
        List<CartItem> cartItems = getCartItems(orderCreateRequest);

        return Price.calculateTotalItemPrice(cartItems);
    }

    public int calculateDiscountedTotalItemPrice(Member member, OrderCreateRequest orderCreateRequest) {
        List<CartItem> cartItems = getCartItems(orderCreateRequest);
        int memberDiscount = member.findDiscountedPercentage();

        return Price.calculateDiscountedTotalItemPrice(cartItems, memberDiscount);
    }

    private int findTotalItemDiscountAmount(OrderCreateRequest orderCreateRequest) {
        int totalItemDiscountAmount = calculateTotalItemDiscountAmount(orderCreateRequest);
        int requestAmount = orderCreateRequest.getTotalItemDiscountAmount();

        return isSamePrice(totalItemDiscountAmount, requestAmount);
    }

    private int calculateTotalItemDiscountAmount(OrderCreateRequest orderCreateRequest) {
        List<CartItem> cartItems = getCartItems(orderCreateRequest);

        return Price.calculateTotalItemDiscountAmount(cartItems);
    }

    public OrderResponse getOrderByIds(Long memberId, Long orderId) {
        Order order = orderDao.findByIds(memberId, orderId);
        List<OrderedItem> orderedItems = orderedItemDao.findByOrderId(order.getId());

        return createOrderResponse(order, orderedItems);
    }

    public List<OrderedItemResponse> convertToResponse(List<OrderedItem> orderedItems) {
        return orderedItems.stream()
                .map(this::createOrderedItemResponse)
                .collect(Collectors.toList());
    }

    private OrderedItemResponse createOrderedItemResponse(OrderedItem orderedItem) {
        return new OrderedItemResponse(orderedItem.getProductQuantity(),
                new ProductResponse(orderedItem.getProductName(), orderedItem.getProductPrice(), orderedItem.getProductImageUrl(),
                        orderedItem.getIsDiscounted(), orderedItem.getDiscountedRate(), orderedItem.calculateDiscountedPrice()));
    }

    public List<OrderResponse> getAllOrders(Long memberId) {
        List<Order> orders = orderDao.findAllByMemberId(memberId);

        List<OrderResponse> orderResponses = new ArrayList<>();
        orders.forEach(order -> {
            List<OrderedItem> orderedItems = orderedItemDao.findByOrderId(order.getId());
            orderResponses.add(createOrderResponse(order, orderedItems));
        });

        return orderResponses;
    }

    private OrderResponse createOrderResponse(Order order, List<OrderedItem> orderedItems) {
        return new OrderResponse(order.getId(), convertToResponse(orderedItems), null, order.getTotalItemPrice(),
                order.getDiscountedTotalItemPrice(), order.getShippingFee(),
                order.getDiscountedTotalItemPrice() + order.getShippingFee());
    }
}
