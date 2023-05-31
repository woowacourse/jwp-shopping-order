package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final MemberRepository memberRepository,
            final ProductRepository productRepository,
            final CartItemRepository cartItemRepository,
            final OrderRepository orderRepository
    ) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public long orderCartItems(final Member member, final OrderRequest request) {
        final int savedPoint = memberRepository.findPointOf(member);
        final int usedPoint = request.getPoints();

        final Map<Product, Integer> products = request.getCartItemIds().stream()
                .collect(Collectors.toMap(
                        id -> {
                            final long productId = cartItemRepository.findProductIdOf(id);
                            return productRepository.getProductById(productId);
                        },
                        cartItemRepository::findQuantityOf
                ));

        final Order order = new Order(savedPoint, usedPoint, products, member);

        final int totalPrice = order.calculateTotalPrice();
        final int orderPrice = order.calculateOrderPrice();

        final int updatedPoint = order.calculatedUpdatedPoint();

        memberRepository.updatePoint(member, updatedPoint);
        final long orderHistoryId = orderRepository.createOrderHistory(member, usedPoint, orderPrice);

        products.keySet()
                .forEach(product -> orderRepository.addOrderProductTo(
                                orderHistoryId, product,
                                products.get(product)
                        )
                );

        return orderHistoryId;
    }

    public List<OrderResponse> getAllOrdersOf(final Member member) {
        orderRepository.findOrdersOf(member);
        return null;
    }
}
