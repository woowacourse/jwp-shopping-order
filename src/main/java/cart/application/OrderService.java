package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import cart.ui.dto.CartItemDto;
import cart.ui.dto.OrderCreateRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public OrderService(final OrderRepository orderRepository,
                        final CartItemRepository cartItemRepository,
                        final MemberRepository memberRepository,
                        final ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Long createOrder(final Member member, final OrderCreateRequest request) {
        final List<OrderItem> orderItems = new ArrayList<>();

        final List<CartItemDto> cartItemDtos = request.getCartItems();
        for (final CartItemDto cartItemDto : cartItemDtos) {
            final CartItem cartItem = cartItemRepository.findById(cartItemDto.getCartItemId());
            final Product product = cartItem.getProduct();
            final OrderItem orderItem = new OrderItem(
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    cartItem.getQuantity(),
                    product.getDiscountRate()
            );
            orderItems.add(orderItem);

            cartItemRepository.deleteById(cartItemDto.getCartItemId());
        }

        final Order order = new Order(orderItems, member);
        final Long savedId = orderRepository.save(order).getId();

        member.addTotalPurchaseAmount(order.calculateTotalPrice());
        member.upgradeRank();
        memberRepository.update(member);
        return savedId;
    }
}
