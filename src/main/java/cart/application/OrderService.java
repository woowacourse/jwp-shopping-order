package cart.application;

import cart.dao.CartItemDao;
import cart.dao.PaymentDao;
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
import cart.exception.PaymentException;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentDao paymentDao;
    private final CartItemDao cartItemDao;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository, PaymentDao paymentDao, CartItemDao cartItemDao) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.paymentDao = paymentDao;
        this.cartItemDao = cartItemDao;
    }

    public Long order(Member member, OrderRequest orderRequest) {
        List<CartItemDto> cartItems = orderRequest.getCartItems();

        for (CartItemDto cartItemDto : cartItems) {
            CartItem cartItem = cartItemDao.findById(cartItemDto.getCartItemId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 cartItem이 존재하지 않습니다."));
            cartItem.checkOwner(member);
            if (cartItem.getQuantity() != cartItemDto.getQuantity()) {
                throw new OrderException("cartItem의 quantity가 기존 cartItem과 일치하지 않습니다. 다시 확인해주세요.");
            }
        }

        List<Product> productsInRequest = cartItems.stream()
                .map(CartItemDto::getProductDto)
                .map(it -> new Product(it.getProductId(), it.getName(), it.getPrice(), it.getImageUrl(), it.getStock()))
                .collect(Collectors.toList());
        List<Long> productIds = cartItems.stream()
                .map(it -> it.getProductDto().getProductId())
                .collect(Collectors.toList());
        List<Product> productsInDb = productRepository.getProductsByIds(productIds);

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

    private static void validatePayment(OrderRequest orderRequest, Payment payment) {
        if (payment.getTotalProductPrice() != orderRequest.getTotalProductPrice()) {
            throw new PaymentException("상품의 총 금액이 다릅니다."
                    + " 입력된 금액 : " + Integer.toString(orderRequest.getTotalProductPrice())
                    + " 실제 금액 : " + Integer.toString(payment.getTotalProductPrice()));
        }
        if (payment.getTotalDeliveryFee() != orderRequest.getTotalDeliveryFee()) {
            throw new PaymentException("총 배달비가 다릅니다.");
        }
        if (payment.getTotalPrice() != orderRequest.getTotalPrice()) {
            throw new PaymentException("주문의 총 금액이 다릅니다.");
        }
    }

    public OrderDto getOrderDetail(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.checkOwner(member);

        Payment payment = paymentDao.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 결제 내역이 존재하지 않습니다."));

        return toDto(order, payment);
    }

    public List<OrderDto> getOrderList(Member member) {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        for (Order order : orders) {
            Payment payment = paymentDao.findByOrderId(order.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 결제 내역이 존재하지 않습니다."));
            orderDtos.add(toDto(order, payment));
        }
        return orderDtos;
    }

    public OrderDto toDto(Order order, Payment payment) {
        return OrderDto.of(order.getId(), order.getOrderDateTime(), order.getOrderItems(), payment.getTotalPrice());
    }
}
