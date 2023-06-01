package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.discount.MemberGradeDiscountPolicy;
import cart.domain.discount.PriceDiscountPolicy;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;

    public OrderService(final CartItemDao cartItemDao, final OrderDao orderDao) {
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public Long save(final Member member, final OrderRequest orderRequest) {
        final List<CartItem> cartItems = cartItemDao.findAllByIds(orderRequest.getCartItemIds());
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));

        final int totalPrice = calculateTotalPrice(member, cartItems);
        final Order order = new Order(member, totalPrice, cartItems);

        final Long orderId = orderDao.save(order).getId();
        cartItemDao.deleteAllByIds(cartItemsToIds(cartItems));

        return orderId;
    }

    private int calculateTotalPrice(final Member member, List<CartItem> cartItems) {
        final int price = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();

        final MemberGradeDiscountPolicy gradeDiscount = new MemberGradeDiscountPolicy(member.getGrade());
        final PriceDiscountPolicy priceDiscount = new PriceDiscountPolicy();
        final int discountAmount = gradeDiscount
                .and(priceDiscount)
                .calculateDiscountAmount(price);

        return price - discountAmount;
    }

    private List<Long> cartItemsToIds(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(toList());
    }

    public List<OrderResponse> findAllByMember(final Member member) {
        final List<Order> orders = orderDao.findAllByMemberId(member.getId());
        return orders.stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    public OrderResponse findById(final Long id) {
        final Order order = orderDao.findById(id);
        return OrderResponse.from(order);
    }
}
