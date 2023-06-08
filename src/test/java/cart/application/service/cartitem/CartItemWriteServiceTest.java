package cart.application.service.cartitem;

import cart.application.cartitem.CartItemWriteService;
import cart.application.cartitem.dto.CartItemCreateDto;
import cart.application.cartitem.dto.CartItemUpdateDto;
import cart.domain.cartitem.CartItem;
import cart.domain.repository.cartitem.CartItemRepository;
import cart.domain.repository.member.MemberRepository;
import cart.domain.repository.product.ProductRepository;
import cart.fixture.MemberFixture;
import cart.fixture.ProductFixture;
import cart.ui.MemberAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static cart.fixture.MemberFixture.레오;
import static cart.fixture.ProductFixture.통구이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class CartItemWriteServiceTest {

    @Autowired
    CartItemWriteService cartItemWriteService;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    long bbqId;
    long tailId;

    long leoId;
    long beaverId;

    long bbqCartId;
    MemberAuth leo;

    @BeforeEach
    void setUp() {
        bbqId = productRepository.createProduct(통구이);
        tailId = productRepository.createProduct(ProductFixture.꼬리요리);

        leoId = memberRepository.createMember(레오);
        beaverId = memberRepository.createMember(MemberFixture.비버);

        leo = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto(bbqId);

        bbqCartId = cartItemWriteService.createCartItem(leo, cartItemCreateDto);
    }

    @Test
    @DisplayName("정상적으로 장바구니에 아이템을 담는다")
    void createCartItem() {
        MemberAuth leo = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto(bbqId);

        Long cartId = cartItemWriteService.createCartItem(leo, cartItemCreateDto);
        CartItem cartItem = cartItemRepository.findById(cartId).get();
        assertAll(
                () -> assertThat(cartId).isPositive(),
                () -> assertThat(cartItem.getProduct().getName()).isEqualTo(통구이.getName()),
                () -> assertThat(cartItem.getMember().getName()).isEqualTo(레오.getName())
        );
    }

    @Test
    @DisplayName("장바구니 수량을 조절한다")
    void updateCartQuantity() {
        CartItemUpdateDto cartItemUpdateDto = new CartItemUpdateDto(2);
        cartItemWriteService.updateQuantity(leo, bbqCartId, cartItemUpdateDto);

        CartItem cartItem = cartItemRepository.findById(bbqCartId).get();
        assertThat(cartItem.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("장바구니 수량이 0으로 업데이터되면 삭제된다")
    void updateToZero() {
        CartItemUpdateDto cartItemUpdateDto = new CartItemUpdateDto(0);
        cartItemWriteService.updateQuantity(leo, bbqCartId, cartItemUpdateDto);

        Optional<CartItem> cartItem = cartItemRepository.findById(bbqCartId);
        assertThat(cartItem).isEmpty();
    }
}
