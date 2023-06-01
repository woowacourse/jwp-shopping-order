package cart.service;

import cart.controller.dto.request.OrderRequest;
import cart.controller.dto.response.OrderThumbnailResponse;
import cart.domain.DiscountPriceCalculator;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.Price;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.exception.NotOwnerException;
import cart.exception.PaymentAmountNotEqualException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import cart.repository.dto.CartItemWithProductDto;
import cart.repository.dto.OrderAndMainProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public long save(final Member member, final OrderRequest request) {
        final List<CartItemWithProductDto> cartItemWithProductDtos = cartItemRepository.findByIds(request.getCartItems());
        checkOwner(member, cartItemWithProductDtos);
        cartItemRepository.deleteAll(request.getCartItems());
        final Order order = createOrder(member, cartItemWithProductDtos);
        validatePaymentAmount(order, request.getPaymentAmount());
        return orderRepository.save(order);
    }

    public List<OrderThumbnailResponse> findByMember(final Member member) {
        final List<OrderAndMainProductDto> dtos = orderRepository.findByMember(member);
        return dtos.stream()
                .map(OrderThumbnailResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    private void checkOwner(final Member member, final List<CartItemWithProductDto> dtos) {
        final boolean isIdEquals = dtos.stream()
                .map(CartItemWithProductDto::getMemberId)
                .allMatch(member::isIdEquals);
        if (isIdEquals) {
            return;
        }
        throw new NotOwnerException();
    }

    private void validatePaymentAmount(final Order order, final int requestPayment) {
        if (order.isPaymentAmountEqual(requestPayment)) {
            return;
        }
        throw new PaymentAmountNotEqualException();
    }

    private Order createOrder(final Member member, final List<CartItemWithProductDto> dtos) {
        return new Order(member,
                new OrderItems(dtos.stream()
                        .map(this::createOrderItem)
                        .collect(Collectors.toUnmodifiableList()),
                        new DiscountPriceCalculator()
                )
        );
    }

    private OrderItem createOrderItem(final CartItemWithProductDto dto) {
        return new OrderItem(
                new Product(dto.getProductId(), dto.getProductName(), new Price(dto.getProductPrice()), dto.getProductImageUrl()),
                new Quantity(dto.getQuantity())
        );
    }
}
