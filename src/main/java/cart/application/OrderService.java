package cart.application;

import cart.domain.Member;
import cart.domain.Product;
import cart.dto.OrderRequest;
import cart.exception.InvalidPointException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final MemberRepository memberRepository,
            final CartItemRepository cartItemRepository,
            final OrderRepository orderRepository
    ) {
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public long orderCartItems(final Member member, final OrderRequest request) {
        final int savedPoint = memberRepository.findPointOf(member);
        final int usedPoint = request.getPoints();
        final int updatedPoint = savedPoint - usedPoint;
        validatePointAmount(updatedPoint);
        memberRepository.updatePoint(member, updatedPoint);

        final Map<Product, Integer> products = request.getCartItemIds().stream()
                .collect(Collectors.toMap(
                        cartItemRepository::findProductOf,
                        cartItemRepository::findQuantityOf
                ));

        final int totalPrice = calculateTotalPrice(products);
        final int orderPrice = totalPrice - usedPoint;

        final long orderHistoryId = orderRepository.createOrderHistory(member, usedPoint, orderPrice);

        for (final Product product : products.keySet()) {
            orderRepository.addOrderProductTo(orderHistoryId, product, products.get(product));
        }

        return orderHistoryId;
    }

    private static int calculateTotalPrice(final Map<Product, Integer> products) {
        return products.keySet().stream()
                .mapToInt(
                        product -> {
                            final int price = product.getPrice();
                            final int quantity = products.get(product);
                            return price * quantity;
                        }
                )
                .sum();
    }

    private static void validatePointAmount(final int updatedPoint) {
        if (updatedPoint < 0) {
            throw new InvalidPointException("존재하는 포인트보다 더 많은 포인트를 사용할 수 없습니다.");
        }
    }
}
