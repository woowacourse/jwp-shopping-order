package cart.service;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.dto.OrderSaveRequest;
import cart.dto.OrdersDto;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 해당멤버의_장바구니에_담긴_상품들을_주문한다() {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 9000L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 9000L));

        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem savedCartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final CartItem savedCartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final OrderSaveRequest request = new OrderSaveRequest(
                List.of(savedCartItem1.getId(), savedCartItem2.getId()), null);
        // when
        Long savedOrderId = orderService.order(member.getId(), request);

        // then
        Order result = orderRepository.findByOrderIdAndMemberId(savedOrderId, member.getId());
        assertThat(result.getCalculateDiscountPrice().intValue()).isEqualTo(18000L);
        assertThat(result.getCoupon()).isEqualTo(Coupon.makeNonDiscountPolicyCoupon());
        assertThat(result.getDeliveryFee()).isEqualTo(3000L);
        assertThat(result.getOrderItems()).hasSize(2);
        assertThat(result.getOrderItems())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new OrderItem(product1.getName(), product1.getPrice(), product1.getImageUrl(), 1),
                        new OrderItem(product2.getName(), product2.getPrice(), product2.getImageUrl(), 1)
                ));

        List<CartItem> findCartItems = cartItemRepository.findAllByMemberId(member.getId());
        assertThat(findCartItems).isEmpty();
    }

    @Test
    void 해당멤버의_모든주문을_조회한다() {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 9000L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 9000L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem savedCartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final CartItem savedCartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final OrderSaveRequest request1 = new OrderSaveRequest(List.of(savedCartItem1.getId()), null);
        final OrderSaveRequest request2 = new OrderSaveRequest(List.of(savedCartItem2.getId()), null);

        Long savedOrderId1 = orderService.order(member.getId(), request1);
        Long savedOrderId2 = orderService.order(member.getId(), request2);

        // when
        List<OrdersDto> result = orderService.findAllByMemberId(member.getId());

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(savedOrderId1);
        assertThat(result.get(1).getId()).isEqualTo(savedOrderId2);
    }

    @Test
    void 해당멤버의_단일_주문을_조회한다() {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 10000L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 9000L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem savedCartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final CartItem savedCartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final OrderSaveRequest request1 = new OrderSaveRequest(List.of(savedCartItem1.getId()), null);
        final OrderSaveRequest request2 = new OrderSaveRequest(List.of(savedCartItem2.getId()), null);

        Long savedOrderId = orderService.order(member.getId(), request1);
        orderService.order(member.getId(), request2);

        // when
        OrdersDto result = orderService.findByOrderId(member.getId(), savedOrderId);

        // then
        assertThat(result.getId()).isEqualTo(savedOrderId);
        assertThat(result.getTotalItemsPrice()).isEqualTo(product1.getPrice());
        assertThat(result.getOrderItems()).hasSize(1);
    }
}
