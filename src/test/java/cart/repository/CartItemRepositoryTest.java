package cart.repository;

import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.MemberFixture.사용자2;
import static cart.fixture.ProductFixture.상품_18900원;
import static cart.fixture.ProductFixture.상품_28900원;
import static cart.fixture.ProductFixture.상품_8900원;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.CartItem;
import cart.domain.cart.Product;
import cart.domain.member.Member;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        final Member member = memberRepository.save(사용자1);
        final Product product = productRepository.save(상품_8900원);
        final CartItem cartItem = new CartItem(member.getId(), product);

        // when
        cartItemRepository.save(cartItem);

        // then
        assertThat(cartItemRepository.findAllByMemberId(member.getId())).hasSize(1);
    }

    @Test
    void 사용자_아이디를_받아_해당_사용자의_장바구니에_있는_모든_품목을_반환한다() {
        // given
        final Member member1 = memberRepository.save(사용자1);
        final Member member2 = memberRepository.save(사용자2);
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_18900원);
        final Product product3 = productRepository.save(상품_28900원);

        cartItemRepository.save(new CartItem(member1.getId(), product1));
        cartItemRepository.save(new CartItem(member1.getId(), product2));
        cartItemRepository.save(new CartItem(member2.getId(), product3));

        // when
        final List<CartItem> result = cartItemRepository.findAllByMemberId(member1.getId());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 삭제할_장바구니의_상품_아이디를_받아_장바구니_항목을_제거한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Product product = productRepository.save(상품_8900원);
        final CartItem cartItem = cartItemRepository.save(new CartItem(member.getId(), product));

        // when
        cartItemRepository.deleteById(cartItem.getId());

        // then
        assertThat(cartItemRepository.findAllByMemberId(member.getId())).isEmpty();
    }

    @Test
    void 삭제할_장바구니의_상품_아이디_목록을_받아_장바구니_항목을_제거한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_18900원);

        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));

        // when
        cartItemRepository.deleteByIds(List.of(cartItem1.getId(), cartItem2.getId()));

        // then
        assertThat(cartItemRepository.findAllByMemberId(member.getId())).isEmpty();
    }

    @Test
    void 장바구니의_상품의_수량을_변경한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Product product = productRepository.save(상품_8900원);
        final CartItem cartItem = cartItemRepository.save(new CartItem(member.getId(), product));

        final CartItem updatedCartItem = new CartItem(
                cartItem.getId(),
                2,
                cartItem.getMemberId(),
                cartItem.getProduct()
        );

        // when
        final CartItem result = cartItemRepository.save(updatedCartItem);

        // then
        assertThat(result.getQuantity()).isEqualTo(2);
    }
}
