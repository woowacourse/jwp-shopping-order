package cart.repository;

import cart.domain.CartItem;
import cart.domain.member.Member;
import cart.domain.Product;
import cart.test.RepositoryTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password1"));
        final Product product = productRepository.save(new Product("치즈피자1", "1.jpg", 8900L));
        final CartItem cartItem = new CartItem(member.getId(), product);

        // when
        final CartItem result = cartItemRepository.save(cartItem);

        // then
        assertThat(cartItemRepository.findAllByMemberId(member.getId())).hasSize(1);
    }

    @Test
    void 사용자_아이디를_받아_해당_사용자의_장바구니에_있는_모든_품목을_반환한다() {
        // given
        final Member member1 = memberRepository.save(new Member("pizza1@pizza.com", "password1"));
        final Member member2 = memberRepository.save(new Member("pizza2@pizza.com", "password2"));
        final Product product1 = productRepository.save(new Product("치즈피자1", "1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("치즈피자2", "2.jpg", 18900L));
        final Product product3 = productRepository.save(new Product("치즈피자3", "3.jpg", 18900L));

        cartItemRepository.save(new CartItem(member1.getId(), product1));
        cartItemRepository.save(new CartItem(member1.getId(), product2));
        cartItemRepository.save(new CartItem(member2.getId(), product3));

        // when
        final List<CartItem> result = cartItemRepository.findAllByMemberId(member1.getId());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 모든_장바구니_항목을_제거한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password1"));
        final Product product1 = productRepository.save(new Product("치즈피자1", "1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("치즈피자2", "2.jpg", 9900L));
        final Product product3 = productRepository.save(new Product("치즈피자3", "3.jpg", 10900L));
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        final CartItem cartItem3 = cartItemRepository.save(new CartItem(member.getId(), product3));

        // when
        cartItemRepository.deleteAll(List.of(cartItem1, cartItem2, cartItem3));

        // then
        assertThat(cartItemRepository.findAllByMemberId(member.getId())).isEmpty();
    }

    @Test
    void 장바구니의_상품의_수량을_변경한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password1"));
        final Product product = productRepository.save(new Product("치즈피자1", "1.jpg", 8900L));
        final CartItem cartItem = cartItemRepository.save(new CartItem(member.getId(), product));

        final CartItem updatedCartItem = new CartItem(cartItem.getId(), 2, cartItem.getMemberId(), cartItem.getProduct());

        // when
        final CartItem result = cartItemRepository.save(updatedCartItem);

        // then
        assertThat(result.getQuantity()).isEqualTo(2);
    }
}
