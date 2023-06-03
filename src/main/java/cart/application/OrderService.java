package cart.application;

import cart.dao.CartItemDao;
import cart.dao.PaymentDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Payment;
import cart.domain.Point;
import cart.domain.Product;
import cart.dto.CartItemDto;
import cart.dto.OrderDto;
import cart.dto.OrderRequest;
import cart.exception.OrderException;
import cart.exception.notFound.CartItemNotFountException;
import cart.exception.notFound.PaymentNotFoundException;
import cart.exception.notFound.ProductNotFoundException;
import cart.exception.payment.TotalDeliveryFeeDoesNotMatchException;
import cart.exception.payment.TotalPriceDoesNotMatchException;
import cart.exception.payment.TotalProductPriceDoesNotMatchException;
import cart.repository.OrderRepository;
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
    private final PaymentDao paymentDao;
    private final OrderRepository orderRepository;

    public OrderService(ProductDao productDao, CartItemDao cartItemDao, PaymentDao paymentDao,
                        OrderRepository orderRepository) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.paymentDao = paymentDao;
        this.orderRepository = orderRepository;
    }

    public Long order(Member member, OrderRequest orderRequest) {
        List<CartItemDto> cartItems = orderRequest.getCartItems();

        for (CartItemDto cartItemDto : cartItems) {
            CartItem cartItem = cartItemDao.findById(cartItemDto.getCartItemId())
                    .orElseThrow(CartItemNotFountException::new);
            cartItem.checkOwner(member);
            if (cartItem.getQuantity() != cartItemDto.getQuantity()) {
                throw new OrderException("cartItem의 quantity가 기존 cartItem과 일치하지 않습니다. 다시 확인해주세요.");
            }
        }

        List<Product> productsInRequest = cartItems.stream()
                .map(CartItemDto::getProduct)
                .map(it -> new Product(it.getProductId(), it.getName(), it.getPrice(), it.getImageUrl(), it.getStock()))
                .collect(Collectors.toList());
        List<Long> productIds = cartItems.stream()
                .map(it -> it.getProduct().getProductId())
                .collect(Collectors.toList());
        List<Product> productsInDb = productIds.stream()
                .map(it -> productDao.findById(it)
                        .orElseThrow(ProductNotFoundException::new))
                .collect(Collectors.toList());

        if (!productsInRequest.equals(productsInDb)) {
            throw new OrderException("product 정보가 업데이트 되었습니다. 다시 확인해주세요.");
        }

        int size = cartItems.size();
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            orderItems.add(OrderItem.of(productsInDb.get(i), cartItems.get(i).getQuantity()));
        }

        Order order = new Order(member, orderItems);
        Payment payment = order.calculatePayment(new Point(orderRequest.getUsePoint()));
        validatePayment(orderRequest, payment);

        List<Long> cartItemIds = cartItems.stream()
                .map(CartItemDto::getCartItemId)
                .collect(Collectors.toList());
        Long orderId = orderRepository.order(cartItemIds, member, order);
        paymentDao.save(orderId, payment);

        return orderId;
    }

    private static void validatePayment(OrderRequest request, Payment payment) {
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

        Payment payment = paymentDao.findByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);

        return toDto(order, payment);
    }

    public List<OrderDto> getOrderList(Member member) {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        for (Order order : orders) {
            Payment payment = paymentDao.findByOrderId(order.getId())
                    .orElseThrow(PaymentNotFoundException::new);
            orderDtos.add(toDto(order, payment));
        }
        return orderDtos;
    }

    private OrderDto toDto(Order order, Payment payment) {
        return OrderDto.of(order.getId(), order.getOrderDateTime(), order.getOrderItems(), payment.getTotalPrice());
    }
}
