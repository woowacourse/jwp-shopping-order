package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.CartItemsDeleteRequest;
import cart.dto.TotalPriceAndDeliveryFeeResponse;
import cart.exception.CartItemException;
import cart.exception.ErrorMessage;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Sql("/truncate.sql")
@SpringBootTest
class CartItemServiceTest {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void 장바구니_상품을_생성한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        CartItemRequest 요청 = new CartItemRequest(상품.getId());

        // when
        Long 저장된_장바구니_상품_ID = cartItemService.save(멤버, 요청);

        // then
        assertThat(cartItemRepository.findById(저장된_장바구니_상품_ID).getProduct().getId()).isEqualTo(상품.getId());
    }

    private Member 멤버를_저장하고_ID가_있는_멤버를_리턴한다(Member 멤버) {
        Long 저장된_멤버_ID = memberRepository.save(멤버);

        return new Member(저장된_멤버_ID, 멤버.getEmail(), 멤버.getPassword(), 멤버.getPoint());
    }

    private Product 상품을_저장하고_ID가_있는_상품을_리턴한다(Product 상품) {
        Long 저장된_상품_ID = productRepository.save(상품);

        return new Product(저장된_상품_ID, 상품.getName(), 상품.getPrice(), 상품.getImageUrl());
    }

    @Test
    void 존재하는_장바구니_상품을_생성하면_수량이_하나_증가한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        CartItemRequest 요청 = new CartItemRequest(상품.getId());
        cartItemService.save(멤버, 요청);

        // when
        Long 저장된_장바구니_상품_ID = cartItemService.save(멤버, 요청);

        // then
        assertThat(cartItemRepository.findById(저장된_장바구니_상품_ID).getQuantity()).isEqualTo(2);
    }

    @Test
    void 멤버의_장바구니_상품들을_조회한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 첫번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        Product 두번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("피자", 20000, "피자이미지"));
        cartItemService.save(멤버, new CartItemRequest(첫번째_상품.getId()));
        cartItemService.save(멤버, new CartItemRequest(두번째_상품.getId()));

        CartItems 장바구니_상품들 = cartItemRepository.findByMemberId(멤버);

        // when
        List<CartItemResponse> 응답 = cartItemService.findByMember(멤버);

        // then
        assertAll(
                () -> assertThat(응답).hasSize(2),
                () -> assertThat(장바구니_상품들.getCartItems()).usingRecursiveComparison()
                        .ignoringFields("member")
                        .isEqualTo(응답)
        );
    }

    @Test
    void 장바구니_상품의_총_가격과_배송비를_반환한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 첫번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        Product 두번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("피자", 20000, "피자이미지"));
        Long 저장된_첫번째_장바구니_상품_ID = cartItemService.save(멤버, new CartItemRequest(첫번째_상품.getId()));
        Long 저장된_두번째_장바구니_상품_ID = cartItemService.save(멤버, new CartItemRequest(두번째_상품.getId()));

        // when
        TotalPriceAndDeliveryFeeResponse 응답 = cartItemService.getTotalPriceAndDeliveryFee(
                멤버,
                List.of(저장된_첫번째_장바구니_상품_ID, 저장된_두번째_장바구니_상품_ID)
        );

        // then
        assertAll(
                () -> assertThat(응답.getTotalPrice()).isEqualTo(30000),
                () -> assertThat(응답.getDeliveryFee()).isEqualTo(3000)
        );
    }

    @Test
    void 장바구니_상품의_수량을_수정한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        Long 저장된_장바구니_상품_ID = cartItemService.save(멤버, new CartItemRequest(상품.getId()));
        CartItemQuantityUpdateRequest 요청 = new CartItemQuantityUpdateRequest(20);

        // when
        cartItemService.updateQuantity(멤버, 저장된_장바구니_상품_ID, 요청);

        // then
        CartItem 저장된_장바구니_상품 = cartItemRepository.findById(저장된_장바구니_상품_ID);
        assertThat(저장된_장바구니_상품.getQuantity()).isEqualTo(20);
    }

    @Test
    void 장바구니_상품을_삭제한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        Long 저장된_장바구니_상품_ID = cartItemService.save(멤버, new CartItemRequest(상품.getId()));

        // when
        cartItemService.deleteCartItem(멤버, 저장된_장바구니_상품_ID);

        // then
        assertThatThrownBy(() -> cartItemRepository.findById(저장된_장바구니_상품_ID))
                .isInstanceOf(CartItemException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_CART_ITEM.getMessage());
    }

    @Test
    void 다른_멤버의_장바구니_상품을_삭제하면_예외를_반환한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Member 다른_멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("other@com", "다른비밀번호", 2000000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        Long 저장된_장바구니_상품_ID = cartItemService.save(멤버, new CartItemRequest(상품.getId()));

        // then
        assertThatThrownBy(() -> cartItemService.deleteCartItem(다른_멤버, 저장된_장바구니_상품_ID))
                .isInstanceOf(CartItemException.class)
                .hasMessage(ErrorMessage.INVALID_CART_ITEM_OWNER.getMessage());
    }


    @Test
    void 장바구니_상품_여러개를_삭제한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 첫번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        Product 두번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("피자", 20000, "피자이미지"));
        Long 저장된_첫번째_장바구니_상품_ID = cartItemService.save(멤버, new CartItemRequest(첫번째_상품.getId()));
        Long 저장된_두번째_장바구니_상품_ID = cartItemService.save(멤버, new CartItemRequest(두번째_상품.getId()));
        CartItemsDeleteRequest 요청 = new CartItemsDeleteRequest(List.of(저장된_첫번째_장바구니_상품_ID, 저장된_두번째_장바구니_상품_ID));

        // when
        cartItemService.deleteCartItems(멤버, 요청);

        // then
        assertAll(
                () -> assertThatThrownBy(() -> cartItemRepository.findById(저장된_첫번째_장바구니_상품_ID))
                        .isInstanceOf(CartItemException.class)
                        .hasMessage(ErrorMessage.NOT_FOUND_CART_ITEM.getMessage()),
                () -> assertThatThrownBy(() -> cartItemRepository.findById(저장된_두번째_장바구니_상품_ID))
                        .isInstanceOf(CartItemException.class)
                        .hasMessage(ErrorMessage.NOT_FOUND_CART_ITEM.getMessage())
        );
    }
}
