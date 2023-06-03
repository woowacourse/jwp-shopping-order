package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.dto.request.PayRequest;
import cart.dto.response.PayResponse;
import cart.exception.InvalidPriceException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PayService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public PayService(
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

    @Transactional
    public PayResponse orderCartItems(final Member member, final PayRequest request) {
        final int savedPoint = memberRepository.findPointOf(member);
        final int usedPoint = request.getPoints();
        final Map<Product, Integer> products = findProductQuantity(request);

        final Order order = new Order(usedPoint, products, member);

        final int originalPrice = order.calculateOriginalPrice();
        validateSamePrice(originalPrice, request.getOriginalPrice());

        final int orderPrice = order.calculateTotalPrice();

        final int updatedPoint = order.calculatedUpdatedPoint(savedPoint);

        memberRepository.updatePoint(member, updatedPoint);
        final long orderHistoryId = orderRepository.createOrderHistory(member, originalPrice, usedPoint, orderPrice);

        products.keySet()
                .forEach(product -> {
                            final Integer quantity = products.get(product);
                            orderRepository.addOrderProductTo(
                                    orderHistoryId,
                                    product,
                                    quantity
                            );
                        }
                );

        return new PayResponse(orderHistoryId);
    }

    @Transactional(readOnly = true)
    private Map<Product, Integer> findProductQuantity(final PayRequest request) {
        return request.getCartItemIds().stream()
                .collect(Collectors.toMap(
                        id -> {
                            final long productId = cartItemRepository.findProductIdOf(id);
                            return productRepository.getProductById(productId);
                        },
                        cartItemRepository::findQuantityOf
                ));
    }

    private static void validateSamePrice(final int originalPrice, final int displayPrice) {
        if (displayPrice != originalPrice) {
            throw new InvalidPriceException("현재 구매 가격과 일치하지 않습니다!");
        }
    }
}
