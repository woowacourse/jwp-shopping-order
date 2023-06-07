package cart.order.application;

import cart.cartitem.dao.CartItemDao;
import cart.cartitem.domain.CartItem;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.application.dto.OrderDto;
import cart.order.application.dto.OrderItemDto;
import cart.order.application.dto.OrderedProductDto;
import cart.order.dao.OrderHistoryDao;
import cart.order.dao.OrderItemDao;
import cart.order.domain.OrderHistory;
import cart.order.ui.request.OrderCartItemRequest;
import cart.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CartItemDao cartItemDao;
    private final OrderHistoryDao orderHistoryDao;
    private final OrderItemDao orderItemDao;
    private final MemberDao memberDao;

    public OrderService(final CartItemDao cartItemDao,
                        final OrderHistoryDao orderHistoryDao,
                        final OrderItemDao orderItemDao,
                        final MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.orderHistoryDao = orderHistoryDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAllByMemberId(final Long memberId) {
        final List<OrderHistory> orderHistories = orderHistoryDao.findByMemberId(memberId);

        final List<OrderDto> orderDtos = orderHistories.stream()
                .map(orderHistory -> findByOrderHistoryId(orderHistory.getId()))
                .collect(Collectors.toList());

        return orderDtos;
    }

    @Transactional(readOnly = true)
    public OrderDto findByOrderHistoryId(final Long orderHistoryId) {
        final OrderHistory orderHistory = orderHistoryDao.findById(orderHistoryId);

        final List<OrderItemDto> orderItemDtos = orderItemDao.findByOrderHistoryId(orderHistoryId);
        final List<OrderedProductDto> products = orderItemDtos.stream()
                .map(OrderedProductDto::from)
                .collect(Collectors.toList());

        return new OrderDto(orderHistory, products);
    }

    @Transactional
    public Long addOrderHistory(final Member member,
                                final List<OrderCartItemRequest> orderCartItemDtos) {

        final Long totalPrice = calculateTotalPrice(orderCartItemDtos);
        final OrderHistory orderHistory = new OrderHistory(member, totalPrice);

        final Long orderHistoryId = orderHistoryDao.save(orderHistory);
        addOrderItems(orderHistoryId, orderCartItemDtos);
        for (OrderCartItemRequest orderCartItemDto : orderCartItemDtos) {
            cartItemDao.deleteById(orderCartItemDto.getCartItemId());
        }
        member.withdraw(totalPrice);
        memberDao.updateMember(member);

        return orderHistoryId;
    }

    private static Long calculateTotalPrice(final List<OrderCartItemRequest> orderCartItemDtos) {
        return orderCartItemDtos.stream()
                .map(OrderCartItemRequest::getOrderCartItemPrice)
                .map(Long::valueOf)
                .reduce(0L, Long::sum);
    }

    private void addOrderItems(final Long orderHistoryId,
                               final List<OrderCartItemRequest> orderCartItemDtos) {
        for (OrderCartItemRequest orderCartItemDto : orderCartItemDtos) {
            validateProductInfo(orderCartItemDto);
            addOrderItem(orderCartItemDto, orderHistoryId);
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

    private void addOrderItem(final OrderCartItemRequest orderCartItemDto, final Long orderHistoryId) {
        final OrderHistory orderHistory = orderHistoryDao.findById(orderHistoryId);
        final CartItem cartItem = cartItemDao.findById(orderCartItemDto.getCartItemId());
        final Long productId = cartItem.getProduct().getId();
        final int quantity = cartItem.getQuantity();

        final OrderItemDto orderItem = OrderItemDto.of(orderHistory, productId, orderCartItemDto, quantity);
        orderItemDao.save(orderItem);
    }
}
