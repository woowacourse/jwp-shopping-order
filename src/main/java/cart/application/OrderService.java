package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderedItem;
import cart.domain.Product;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemRequest;
import cart.dto.OrderResponse;
import cart.dto.OrderedItemResponse;
import cart.dto.ProductResponse;
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
        //totalItemPrice = 상품의 원가(할인 적용 전) + 배송비
        int totalItemPrice = findTotalItemPrice(orderCreateRequest);
        //shippingFee 구하기 -> totalPrice로 구함
        int shippingFee = Order.calculateShippingFee(totalItemPrice);
        //discountedTotalPrice 구하기 -> 상품 할인 체크 -> 상품 할인이 있다면 상품 할인 적용, 아니라면 member의 등급 확인해서 구함
        int discountedTotalPrice = findDiscountedTotalItemPrice(member, orderCreateRequest, totalItemPrice);
        int totalPurchaseAmount = totalItemPrice + shippingFee;

        Order order = new Order(member.getId(), totalPurchaseAmount, totalItemPrice, null, shippingFee, discountedTotalPrice);
        member.order(totalPurchaseAmount);
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

    public List<CartItem> findCartItems(OrderCreateRequest orderCreateRequest) {
        List<OrderItemRequest> orderItemRequests = orderCreateRequest.getOrderItemRequests();
        List<CartItem> cartItems = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            CartItem cartItem = cartItemDao.findById(orderItemRequest.getCartItemId());
            cartItems.add(cartItem);
        }

        return cartItems;
    }


    public int findTotalItemPrice(OrderCreateRequest orderCreateRequest) {
        List<OrderItemRequest> orderItemRequests = orderCreateRequest.getOrderItemRequests();
        List<Integer> prices = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            CartItem cartItem = cartItemDao.findById(orderItemRequest.getCartItemId());
            int price = cartItem.getProduct().getPrice();
            prices.add(price);
        }

        return Order.calculatePriceSum(prices);
    }

    public int findDiscountedTotalItemPrice(Member member, OrderCreateRequest orderCreateRequest, int totalItemPrice) {
        List<OrderItemRequest> orderItemRequests = orderCreateRequest.getOrderItemRequests();
        List<Integer> discountedPrice = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            CartItem cartItem = cartItemDao.findById(orderItemRequest.getCartItemId());
            Product product = cartItem.getProduct();
            int memberDiscount = member.calculateDiscountedPercentage();

            int price = product.calculateDiscountedPrice(memberDiscount);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

    public OrderResponse getOrderByIds(Long memberId, Long orderId) {
        Order order = orderDao.findByIds(memberId, orderId);
        List<OrderedItem> orderedItems = orderedItemDao.findByOrderId(order.getId());
        return new OrderResponse(order.getId(), convertToResponse(orderedItems), null, order.getTotalItemPrice(),
                order.getDiscountedTotalPrice(), order.getShippingFee(),
                order.getDiscountedTotalPrice() + order.getShippingFee());
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
                order.getDiscountedTotalPrice(), order.getShippingFee(),
                order.getDiscountedTotalPrice() + order.getShippingFee());
    }
}
