package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderHistoryDao;
import cart.dao.OrderProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.domain.OrderProduct;
import cart.domain.Point;
import cart.dto.request.CartItemIdRequest;
import cart.dto.request.PaymentRequest;
import cart.dto.response.OrderIdResponse;
import cart.exception.ErrorStatus;
import cart.exception.ShoppingOrderException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PayService {

    private final CartItemDao cartItemDao;
    private final OrderHistoryDao orderHistoryDao;
    private final OrderProductDao orderProductDao;
    private final MemberDao memberDao;

    public PayService(final CartItemDao cartItemDao, final OrderHistoryDao orderHistoryDao,
                      final OrderProductDao orderProductDao, final MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.orderHistoryDao = orderHistoryDao;
        this.orderProductDao = orderProductDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public OrderIdResponse pay(final Member member, final PaymentRequest paymentRequest) {
        validatePoint(member, paymentRequest);
        List<CartItem> cartItems = getCartItems(paymentRequest);
        validatePrice(paymentRequest, cartItems);

        OrderHistory orderHistory = getOrderHistory(member, paymentRequest);

        List<OrderProduct> orderProducts = makeOrderProducts(cartItems, orderHistory);
        orderProductDao.insertAll(orderProducts);

        updatePoint(member, paymentRequest);
        return new OrderIdResponse(orderHistory.getId());
    }

    private void validatePoint(final Member member, final PaymentRequest paymentRequest) {
        Integer requestPoints = paymentRequest.getPoints();
        if (requestPoints > paymentRequest.getOriginalPrice()) {
            throw new ShoppingOrderException(ErrorStatus.POINT_OVER_PRICE);
        }
        Point point = member.getPoint();
        if (point.isLessThan(requestPoints)) {
            throw new ShoppingOrderException(ErrorStatus.POINT_NOT_ENOUGH);
        }
    }

    private List<CartItem> getCartItems(final PaymentRequest paymentRequest) {
        List<Long> cartItemIds = getCartItemsIdsFrom(paymentRequest);
        List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);
        return cartItems;
    }

    private List<Long> getCartItemsIdsFrom(final PaymentRequest paymentRequest) {
        return paymentRequest.getCartItemIds().stream()
                .map(CartItemIdRequest::getCartItemId)
                .collect(Collectors.toList());
    }

    private void validatePrice(final PaymentRequest paymentRequest, final List<CartItem> cartItems) {
        Integer originalPrice = paymentRequest.getOriginalPrice();
        int totalPrice = getTotalPrice(cartItems);
        if (!originalPrice.equals(totalPrice)) {
            throw new ShoppingOrderException(ErrorStatus.PAYMENT_PRICE_INVALID);
        }
    }

    private int getTotalPrice(final List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();
    }

    private OrderHistory getOrderHistory(final Member member, final PaymentRequest paymentRequest) {
        Integer originalPrice = paymentRequest.getOriginalPrice();
        Integer requestPoints = paymentRequest.getPoints();
        OrderHistory orderHistory = new OrderHistory(originalPrice, requestPoints, originalPrice - requestPoints,
                member);
        Long orderHistoryId = orderHistoryDao.insert(orderHistory);
        return new OrderHistory(orderHistoryId, originalPrice, requestPoints, originalPrice - requestPoints,
                member);
    }

    private List<OrderProduct> makeOrderProducts(final List<CartItem> cartItems, final OrderHistory orderHistory) {
        return cartItems.stream()
                .map(cartItem -> new OrderProduct(orderHistory, cartItem.getProduct().getId(),
                        cartItem.getProduct().getName(), cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getImageUrl(), cartItem.getQuantity()))
                .collect(Collectors.toList());
    }

    private void updatePoint(final Member member, final PaymentRequest paymentRequest) {
        Point newPoint = member.getPoint()
                .use(paymentRequest.getPoints())
                .collect(paymentRequest.getOriginalPrice() - paymentRequest.getPoints());
        member.updatePoint(newPoint);
        memberDao.update(member);
    }
}
