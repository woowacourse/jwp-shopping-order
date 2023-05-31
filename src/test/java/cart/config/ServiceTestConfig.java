package cart.config;

import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;

public abstract class ServiceTestConfig extends RepositoryTestConfig {

    protected ProductRepository productRepository;
    protected CartItemRepository cartItemRepository;
    protected MemberRepository memberRepository;

    @BeforeEach
    void serviceSetUp() {
        productRepository = new ProductRepository(productDao);
        cartItemRepository = new CartItemRepository(cartItemDao);
        memberRepository = new MemberRepository(memberDao);
    }
}
