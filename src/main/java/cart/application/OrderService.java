package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CartOrderDao;
import cart.dao.MemberDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartOrder;
import cart.domain.Member;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.OrderCartItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final CartItemDao cartItemDao;
    private final CartOrderDao cartOrderDao;
    private final OrderItemDao orderItemDao;

    public OrderService(final CartItemDao cartItemDao, final CartOrderDao cartOrderDao, final OrderItemDao orderItemDao) {
        this.cartItemDao = cartItemDao;
        this.cartOrderDao = cartOrderDao;
        this.orderItemDao = orderItemDao;
    }

    public List<CartOrder> findByMember(final Member member) {
        return cartOrderDao.findByMemberId(member.getId());
    }

    @Transactional
    public Long addCartOrder(final Member member,
                             final List<OrderCartItemDto> orderCartItemDtos) {

        final Long totalPrice = calculateTotalPrice(orderCartItemDtos);
        final CartOrder cartOrder = new CartOrder(member, totalPrice);

        final Long cartOrderId = cartOrderDao.save(cartOrder);
        addOrderItems(cartOrderId, orderCartItemDtos);

        return cartOrderId;
    }

    private static Long calculateTotalPrice(final List<OrderCartItemDto> orderCartItemDtos) {
        return orderCartItemDtos.stream()
                .map(OrderCartItemDto::getOrderCartItemPrice)
                .map(Long::valueOf)
                .reduce(0L, Long::sum);
    }

    private void addOrderItems(final Long cartOrderId,
                               final List<OrderCartItemDto> orderCartItemDtos) {
        for (OrderCartItemDto orderCartItemDto : orderCartItemDtos) {
            validateProductInfo(orderCartItemDto);
            addOrderItem(orderCartItemDto, cartOrderId);
        }
    }

    private void validateProductInfo(OrderCartItemDto orderCartItemDto) {
        final CartItem cartItem = cartItemDao.findById(orderCartItemDto.getCartItemId());
        final Product originProduct = cartItem.getProduct();

        if (!originProduct.getName().equals(orderCartItemDto.getOrderCartItemName())
                || originProduct.getPrice() != orderCartItemDto.getOrderCartItemPrice()
                || !originProduct.getImageUrl().equals(orderCartItemDto.getOrderCartItemImageUrl())) {
            throw new IllegalArgumentException("상품 정보 실패");
        }
    }

    private void addOrderItem(final OrderCartItemDto orderCartItemDto, final Long cartOrderId) {
        final CartOrder cartOrder = cartOrderDao.findById(cartOrderId);
        final CartItem cartItem = cartItemDao.findById(orderCartItemDto.getCartItemId());
        final int quantity = cartItem.getQuantity();
        final OrderItem orderItem = OrderItem.of(cartOrder, orderCartItemDto, quantity);
        orderItemDao.save(orderItem);
    }
}
