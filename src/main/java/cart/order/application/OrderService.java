package cart.order.application;

import cart.cartItem.domain.CartItem;
import cart.cartItem.persistence.CartItemDao;
import cart.order.application.dto.OrderAddDto;
import cart.order.application.dto.OrderDto;
import cart.order.application.dto.CartItemDto;
import cart.common.notFoundException.CartItemNotFountException;
import cart.common.notFoundException.ProductNotFoundException;
import cart.order.exception.order.ProductInfoDoesNotMatchException;
import cart.order.exception.payment.TotalDeliveryFeeDoesNotMatchException;
import cart.order.exception.payment.TotalPriceDoesNotMatchException;
import cart.order.exception.payment.TotalProductPriceDoesNotMatchException;
import cart.member.domain.Member;
import cart.member.domain.Point;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.Payment;
import cart.order.persistence.OrderRepository;
import cart.order.persistence.PaymentRepository;
import cart.product.domain.Product;
import cart.product.persistence.ProductDao;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductDao productDao, CartItemDao cartItemDao, PaymentRepository paymentRepository,
                        OrderRepository orderRepository) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public Long order(Member member, OrderAddDto orderAddDto) {
        List<CartItemDto> requestCartItems = orderAddDto.getCartItems();

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDto requestCartItem : requestCartItems) {
            validateCartItem(member, requestCartItem);
            validateProductInCartItem(requestCartItem);

            Product product = productDao.findById(requestCartItem.getProduct().getProductId()).get();
            orderItems.add(OrderItem.of(product, requestCartItem.getQuantity()));
        }

        Order order = Order.makeOrder(member, orderItems);
        Payment payment = Payment.makePayment(order, member, new Point(orderAddDto.getUsePoint()));
        validatePayment(orderAddDto, payment);

        List<Long> cartItemIds = requestCartItems.stream()
                .map(CartItemDto::getCartItemId)
                .collect(Collectors.toList());
        Long orderId = orderRepository.order(cartItemIds, member.getId(), order);
        paymentRepository.payment(orderId, payment, member);

        return orderId;
    }

    private void validateCartItem(Member member, CartItemDto requestCartItem) {
        CartItem realCartItem = cartItemDao.findById(requestCartItem.getCartItemId())
                .orElseThrow(CartItemNotFountException::new);
        realCartItem.checkOwner(member);
        realCartItem.checkQuantity(requestCartItem.getQuantity());
    }

    private void validateProductInCartItem(CartItemDto requestCartItem) {
        Product requestProduct = requestCartItem.getProduct().toDomain();
        Product realProduct = productDao.findById(requestProduct.getId())
                .orElseThrow(ProductNotFoundException::new);
        if (!requestProduct.equals(realProduct)) {
            throw new ProductInfoDoesNotMatchException();
        }
    }

    private static void validatePayment(OrderAddDto request, Payment payment) {
        if (payment.getTotalProductPrice() != request.getTotalProductPrice()) {
            throw new TotalProductPriceDoesNotMatchException(request.getTotalProductPrice(),
                    payment.getTotalProductPrice());
        }
        if (payment.getTotalDeliveryFee() != request.getTotalDeliveryFee()) {
            throw new TotalDeliveryFeeDoesNotMatchException(request.getTotalDeliveryFee(),
                    payment.getTotalDeliveryFee());
        }
        if (payment.getTotalPrice() != request.getTotalPrice()) {
            throw new TotalPriceDoesNotMatchException(request.getTotalPrice(), payment.getTotalPrice());
        }
    }

    public OrderDto getOrderDetail(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.checkOwner(member);

        Payment payment = paymentRepository.findByOrderId(orderId);

        return toDto(order, payment);
    }

    public List<OrderDto> getOrderList(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());

        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            Payment payment = paymentRepository.findByOrderId(order.getId());
            orderDtos.add(toDto(order, payment));
        }

        return orderDtos;
    }

    private OrderDto toDto(Order order, Payment payment) {
        return OrderDto.of(order.getId(), order.getOrderDateTime(), order.getOrderItems(), payment.getTotalPrice());
    }
}
