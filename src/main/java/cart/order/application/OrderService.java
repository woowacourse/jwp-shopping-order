package cart.order.application;

import cart.cartItem.application.CartItemRepository;
import cart.cartItem.domain.CartItem;
import cart.common.notFoundException.CartItemNotFountException;
import cart.common.notFoundException.OrderNotFoundException;
import cart.common.notFoundException.PaymentNotFoundException;
import cart.common.notFoundException.ProductNotFoundException;
import cart.member.application.MemberRepository;
import cart.member.domain.Member;
import cart.member.domain.Point;
import cart.order.application.dto.CartItemDto;
import cart.order.application.dto.OrderAddDto;
import cart.order.application.dto.OrderDto;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.Payment;
import cart.order.exception.order.ProductInfoDoesNotMatchException;
import cart.order.exception.payment.TotalDeliveryFeeDoesNotMatchException;
import cart.order.exception.payment.TotalPriceDoesNotMatchException;
import cart.order.exception.payment.TotalProductPriceDoesNotMatchException;
import cart.product.application.ProductRepository;
import cart.product.domain.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(MemberRepository memberRepository, ProductRepository productRepository,
                        CartItemRepository cartItemRepository, PaymentRepository paymentRepository,
                        OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Long order(Member member, OrderAddDto orderAddDto) {
        List<OrderItem> orderItems = makeOrderItems(member, orderAddDto);
        Order order = Order.makeOrder(member, orderItems);
        Payment payment = Payment.makePayment(order, member, new Point(orderAddDto.getUsePoint()));
        validatePayment(orderAddDto, payment);

        return saveOrder(orderAddDto.getCartItems(), member, order, payment);
    }

    private List<OrderItem> makeOrderItems(Member member, OrderAddDto orderAddDto) {
        List<CartItemDto> requestCartItems = orderAddDto.getCartItems();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDto requestCartItem : requestCartItems) {
            validateCartItem(member, requestCartItem);
            validateProductInCartItem(requestCartItem);

            Product product = productRepository.findById(requestCartItem.getProduct().getProductId()).get();
            orderItems.add(OrderItem.of(product, requestCartItem.getQuantity()));
        }
        return orderItems;
    }

    private void validateCartItem(Member member, CartItemDto requestCartItem) {
        CartItem realCartItem = cartItemRepository.findById(requestCartItem.getCartItemId())
                .orElseThrow(CartItemNotFountException::new);
        realCartItem.checkOwner(member);
        realCartItem.checkQuantity(requestCartItem.getQuantity());
    }

    private void validateProductInCartItem(CartItemDto requestCartItem) {
        Product requestProduct = requestCartItem.getProduct().toDomain();
        Product realProduct = productRepository.findById(requestProduct.getId())
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

    private Long saveOrder(List<CartItemDto> cartItemDtos, Member member, Order order, Payment payment) {
        deleteCartItems(cartItemDtos);
        updateProductStocks(order);
        Long orderId = orderRepository.save(member.getId());
        orderItemRepository.saveAll(orderId, order.getOrderItems());
        paymentRepository.save(orderId, payment);
        memberRepository.updatePoint(member);
        return orderId;
    }

    private void deleteCartItems(List<CartItemDto> requestCartItems) {
        List<Long> cartItemIds = requestCartItems.stream()
                .map(CartItemDto::getCartItemId)
                .collect(Collectors.toList());
        cartItemRepository.deleteByIds(cartItemIds);
    }

    private void updateProductStocks(Order order) {
        List<Product> products = order.getOrderItems()
                .stream()
                .map(OrderItem::getOriginalProduct)
                .collect(Collectors.toList());
        productRepository.updateStocks(products);
    }

    public OrderDto getOrderDetail(Member member, Long orderId) {
        Order order = getAssembledOrder(orderId, member);
        order.checkOwner(member);

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);

        return toDto(order, payment);
    }

    public List<OrderDto> getOrderList(Member member) {
        List<Order> orders = getAssembledOrders(member);

        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            Payment payment = paymentRepository.findByOrderId(order.getId())
                    .orElseThrow(PaymentNotFoundException::new);
            orderDtos.add(toDto(order, payment));
        }

        return orderDtos;
    }

    private Order getAssembledOrder(Long orderId, Member member) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        return new Order(order.getId(), order.getMember(), orderItems, order.getOrderDateTime());
    }

    private List<Order> getAssembledOrders(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        return orders.stream()
                .map(it -> getAssembledOrder(it.getId(), member))
                .collect(Collectors.toList());
    }

    private OrderDto toDto(Order order, Payment payment) {
        return OrderDto.of(order.getId(), order.getOrderDateTime(), order.getOrderItems(), payment.getTotalPrice());
    }
}
