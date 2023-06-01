package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CartOrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.CartOrder;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.OrderCartItemDto;
import cart.dto.OrderDto;
import cart.dto.OrderItemDto;
import cart.dto.OrderedProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<OrderDto> findAllByMemberId(final Long memberId) {
        final List<CartOrder> cartOrders = cartOrderDao.findByMemberId(memberId);

        final List<OrderDto> orderDtos = cartOrders.stream()
                .map(cartOrder -> findByCartOrderId(cartOrder.getId()))
                .collect(Collectors.toList());

        return orderDtos;
    }

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
                             final List<OrderCartItemDto> orderCartItemDtos) {

        final Long totalPrice = calculateTotalPrice(orderCartItemDtos);
        final CartOrder cartOrder = new CartOrder(member, totalPrice);

        final Long cartOrderId = cartOrderDao.save(cartOrder);
        addOrderItems(cartOrderId, orderCartItemDtos);
        for (OrderCartItemDto orderCartItemDto : orderCartItemDtos) {
            cartItemDao.deleteById(orderCartItemDto.getCartItemId());
        }

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
        final Long productId = cartItem.getProduct().getId();
        final int quantity = cartItem.getQuantity();

        final OrderItemDto orderItem = OrderItemDto.of(cartOrder, productId, orderCartItemDto, quantity);
        orderItemDao.save(orderItem);
    }
}
