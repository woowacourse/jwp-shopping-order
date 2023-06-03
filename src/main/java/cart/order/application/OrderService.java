package cart.order.application;

import cart.cartitem.dao.CartItemDao;
import cart.cartitem.domain.CartItem;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.application.dto.OrderDto;
import cart.order.application.dto.OrderItemDto;
import cart.order.application.dto.OrderedProductDto;
import cart.order.dao.CartOrderDao;
import cart.order.dao.OrderItemDao;
import cart.order.domain.CartOrder;
import cart.order.ui.request.OrderCartItemRequest;
import cart.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CartItemDao cartItemDao;
    private final CartOrderDao cartOrderDao;
    private final OrderItemDao orderItemDao;
    private final MemberDao memberDao;

    public OrderService(final CartItemDao cartItemDao, final CartOrderDao cartOrderDao, final OrderItemDao orderItemDao, final MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.cartOrderDao = cartOrderDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAllByMemberId(final Long memberId) {
        final List<CartOrder> cartOrders = cartOrderDao.findByMemberId(memberId);

        final List<OrderDto> orderDtos = cartOrders.stream()
                .map(cartOrder -> findByCartOrderId(cartOrder.getId()))
                .collect(Collectors.toList());

        return orderDtos;
    }

    @Transactional(readOnly = true)
    public OrderDto findByCartOrderId(final Long cartOrderId) {
        final CartOrder cartOrder = cartOrderDao.findById(cartOrderId);

        final List<OrderItemDto> orderItemDtos = orderItemDao.findByCartOrderId(cartOrderId);
        final List<OrderedProductDto> products = orderItemDtos.stream()
                .map(OrderedProductDto::from)
                .collect(Collectors.toList());

        return new OrderDto(cartOrder, products);
    }

    @Transactional
    public Long addCartOrder(final Member member,
                             final List<OrderCartItemRequest> orderCartItemDtos) {

        final Long totalPrice = calculateTotalPrice(orderCartItemDtos);
        final CartOrder cartOrder = new CartOrder(member, totalPrice);

        final Long cartOrderId = cartOrderDao.save(cartOrder);
        addOrderItems(cartOrderId, orderCartItemDtos);
        for (OrderCartItemRequest orderCartItemDto : orderCartItemDtos) {
            cartItemDao.deleteById(orderCartItemDto.getCartItemId());
        }
        member.withdraw(totalPrice);
        memberDao.updateMember(member);

        return cartOrderId;
    }

    private static Long calculateTotalPrice(final List<OrderCartItemRequest> orderCartItemDtos) {
        return orderCartItemDtos.stream()
                .map(OrderCartItemRequest::getOrderCartItemPrice)
                .map(Long::valueOf)
                .reduce(0L, Long::sum);
    }

    private void addOrderItems(final Long cartOrderId,
                               final List<OrderCartItemRequest> orderCartItemDtos) {
        for (OrderCartItemRequest orderCartItemDto : orderCartItemDtos) {
            validateProductInfo(orderCartItemDto);
            addOrderItem(orderCartItemDto, cartOrderId);
        }
    }

    private void validateProductInfo(OrderCartItemRequest orderCartItemDto) {
        final CartItem cartItem = cartItemDao.findById(orderCartItemDto.getCartItemId());
        final Product originProduct = cartItem.getProduct();

        if (!originProduct.getName().equals(orderCartItemDto.getOrderCartItemName())
                || originProduct.getPrice() != orderCartItemDto.getOrderCartItemPrice()
                || !originProduct.getImageUrl().equals(orderCartItemDto.getOrderCartItemImageUrl())) {
            throw new IllegalArgumentException("상품 정보 실패");
        }
    }

    private void addOrderItem(final OrderCartItemRequest orderCartItemDto, final Long cartOrderId) {
        final CartOrder cartOrder = cartOrderDao.findById(cartOrderId);
        final CartItem cartItem = cartItemDao.findById(orderCartItemDto.getCartItemId());
        final Long productId = cartItem.getProduct().getId();
        final int quantity = cartItem.getQuantity();

        final OrderItemDto orderItem = OrderItemDto.of(cartOrder, productId, orderCartItemDto, quantity);
        orderItemDao.save(orderItem);
    }
}
