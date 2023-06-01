package cart.config;

import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public abstract class ServiceTestConfig extends RepositoryTestConfig {

    protected ProductRepository productRepository;
    protected CartItemRepository cartItemRepository;
    protected MemberRepository memberRepository;
    protected OrderRepository orderRepository;

    @Autowired
    protected ApplicationEventPublisher publisher;

    @BeforeEach
    void serviceSetUp() {
        productRepository = new ProductRepository(productDao);
        cartItemRepository = new CartItemRepository(cartItemDao);
        memberRepository = new MemberRepository(memberDao);
        orderRepository = new OrderRepository(orderDao, orderItemDao);
    }
}
