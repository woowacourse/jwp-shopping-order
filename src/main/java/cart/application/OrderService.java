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
        //TODO: 가격들 validate 추가

        //totalItemPrice = 상품의 원가(할인 적용 전)
        int totalItemPrice = findTotalItemPrice(orderCreateRequest);
        //shippingFee = 배송비
        int shippingFee = Order.calculateShippingFee(totalItemPrice);
        //discountedItemTotalPrice = 할인이 적용된 상품들의 가격
        int discountedItemTotalPrice = findDiscountedTotalItemPrice(member, orderCreateRequest, totalItemPrice);
        int totalPurchaseAmount = totalItemPrice + shippingFee;

        Order order = new Order(member.getId(), totalPurchaseAmount, totalItemPrice, null, shippingFee, discountedItemTotalPrice);
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

    private int findTotalItemPrice(OrderCreateRequest orderCreateRequest) {
        int price = calculateTotalItemPrice(orderCreateRequest);
        int requestPrice = orderCreateRequest.getTotalItemPrice();

        if(price == requestPrice){
            return price;
        }

        throw new PriceValidationException();
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

    public int findDiscountedTotalItemPrice(Member member, OrderCreateRequest orderCreateRequest, int totalItemPrice) {
        List<Integer> discountedPrice = new ArrayList<>();
        for (Long cartItemId : orderCreateRequest.getCartItemIds()) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
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
