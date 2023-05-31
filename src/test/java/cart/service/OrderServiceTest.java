package cart.service;

import cart.domain.CartItem;
import cart.domain.Money;
import cart.domain.Product;
import cart.domain.member.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 주문을_저장한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", new Money(9900L)));
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItem1.getId(), cartItem2.getId()));

        // when
        orderService.save(member.getId(), orderRequest);

        // then
        final List<OrderResponse> orderResponses = orderService.findAll(member.getId());
        assertThat(orderResponses).hasSize(1);
    }

    @Test
    void 사용자_아이디에_해당하는_모든_주문을_조회한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", new Money(9900L)));
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItem1.getId(), cartItem2.getId()));
        orderService.save(member.getId(), orderRequest);

        // when
        final List<OrderResponse> orderResponses = orderService.findAll(member.getId());

        // then
        assertThat(orderResponses).hasSize(1);
    }

    @Test
    void 단일_주문을_조회한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", new Money(9900L)));
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItem1.getId(), cartItem2.getId()));
        final Long order = orderService.save(member.getId(), orderRequest);

        // when
        final OrderResponse response = orderService.findById(member.getId(), order);

        // then
        assertThat(response.getId()).isEqualTo(order);
    }
}