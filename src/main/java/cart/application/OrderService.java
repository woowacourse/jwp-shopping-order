package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public OrderService(final OrderDao orderDao, final CartItemDao cartItemDao, final ProductDao productDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    //TODO: cartItem Id는 주문 성공시 삭제할 떄 사용, productId로 정보를 불러와서 Order에 저장, quantity 정보가 db와 일치하는지 확인
    public Long createOrder(final OrderCreateRequest orderCreateRequest, final Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberIdAndChecked(member.getId());
        final List<CartItemRequest> requests = orderCreateRequest.getCartItems();

        for (final CartItem cartItem : cartItems) {
            iterateRequests(requests, cartItem);
        }

        final List<CartItem> cartItemsByRequest = toCartItems(member, requests);

        for (final CartItem cartItem : cartItemsByRequest) {
            final Long id = cartItem.getId();
            cartItemDao.deleteById(id);
        }

        return orderDao.createOrder(orderCreateRequest.getUsedPoints(), cartItemsByRequest, member);
    }

    private List<CartItem> toCartItems(final Member member, final List<CartItemRequest> requests) {
        return requests.stream()
                .map(cartItemRequest -> new CartItem(cartItemRequest.getId(), cartItemRequest.getQuantity(),
                        productDao.getProductById(cartItemRequest.getProductId()),
                        member,
                        true
                )).collect(Collectors.toList());
    }

    private void iterateRequests(final List<CartItemRequest> requests, final CartItem cartItem) {
        for (final CartItemRequest request : requests) {
            compareEachCartItemIfIdEquals(cartItem, request);
        }
    }

    private void compareEachCartItemIfIdEquals(final CartItem cartItem, final CartItemRequest request) {
        if (cartItem.getId().equals(request.getId())) {
            compareEachCartItem(cartItem, request);
        }
    }

    private void compareEachCartItem(final CartItem cartItem, final CartItemRequest request) {
        if (isInvalidProduct(cartItem, request) || isInvalidQuantity(cartItem, request) || isNotChecked(cartItem)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isInvalidProduct(final CartItem cartItem, final CartItemRequest request) {
        return !cartItem.getProduct().getId().equals(request.getProductId());
    }

    private boolean isInvalidQuantity(final CartItem cartItem, final CartItemRequest request) {
        return cartItem.getQuantity() != request.getQuantity();
    }

    private boolean isNotChecked(final CartItem cartItem) {
        return !cartItem.isChecked();
    }
}
