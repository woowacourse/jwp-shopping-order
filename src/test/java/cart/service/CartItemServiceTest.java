package cart.service;

import cart.domain.CartItem;
import cart.domain.Money;
import cart.domain.member.Member;
import cart.domain.Product;
import cart.dto.CartItemDto;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.ProductDto;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class CartItemServiceTest {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        final Product product = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem cartItem = new CartItem(member.getId(), product);

        // when
        cartItemRepository.save(cartItem);

        // then
        final List<CartItem> cartItems = cartItemRepository.findAllByMemberId(member.getId());
        assertThat(cartItems).hasSize(1);
    }

    @Test
    void 입력받은_사용자의_카트에_담겨있는_모든_상품을_조회한다() {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", new Money(18900L)));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));

        // when
        final List<CartItemDto> result = cartItemService.findAll(member.getId());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new CartItemDto(cartItem1.getId(), 1, ProductDto.from(product1)),
                new CartItemDto(cartItem2.getId(), 1, ProductDto.from(product2))
        ));
    }

    @Test
    void 삭제할_품목_아이디와_사용자_아이디를_받아_장바구니_항목을_제거한다() {
        // given
        final Product product = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem cartItem = cartItemRepository.save(new CartItem(member.getId(), product));

        // when
        cartItemService.delete(cartItem.getId(), member.getId());

        // then
        assertThat(cartItemRepository.findAllByMemberId(member.getId())).isEmpty();
    }

    @Test
    void 상품_수량을_변경한다() {
        // given
        final Product product = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem cartItem = cartItemRepository.save(new CartItem(member.getId(), product));
        final CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(2);

        // when
        cartItemService.updateQuantity(member.getId(), cartItem.getId(), request);

        // then
        final CartItem result = cartItemRepository.findById(cartItem.getId()).get();
        assertThat(result.getQuantity()).isEqualTo(2);
    }

    @Test
    void 상품_수량을_0으로_변경하는_경우_장바구니에서_삭제된다() {
        // given
        final Product product = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem cartItem = cartItemRepository.save(new CartItem(member.getId(), product));
        final CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(0);

        // when
        cartItemService.updateQuantity(member.getId(), cartItem.getId(), request);

        // then
        assertThat(cartItemRepository.findById(cartItem.getId())).isNotPresent();
    }
}
