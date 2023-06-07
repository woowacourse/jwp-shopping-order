package cart.service.cart;

import cart.domain.cart.Cart;
import cart.domain.member.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.repository.cart.CartRepository;
import cart.repository.product.ProductRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixture.MemberFixture.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/setData.sql")
public class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DisplayName("모든 장바구니를 조회한다.")
    @Test
    void find_all_items() {
        // given
        Member member = createMember();

        // when
        List<CartItemResponse> result = cartService.findAllCartItems(member);

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.get(0).getQuantity()).isEqualTo(10)
        );
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void add_item() {
        // given
        Member member = createMember();
        cartService.deleteAllCartItems(member);
        CartItemRequest req = new CartItemRequest(1L);

        // when
        cartService.add(member, req);

        // then
        List<CartItemResponse> result = cartService.findAllCartItems(member);
        assertAll(
                () -> assertThat(result.size()).isEqualTo(1),
                () -> assertThat(result.get(0).getQuantity()).isEqualTo(1)
        );
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 증가시킨다.")
    @Test
    void update_item_quantity() {
        // given
        Member member = createMember();
        Cart memberCart = cartRepository.findCartByMemberId(member.getId());
        long id = memberCart.getCartItems().get(0).getId();
        CartItemQuantityUpdateRequest req = new CartItemQuantityUpdateRequest(100);

        // when
        cartService.updateQuantity(member, id, req);

        // then
        List<CartItemResponse> result = cartService.findAllCartItems(member);
        assertThat(result.get(0).getQuantity()).isEqualTo(req.getQuantity());
    }

    @DisplayName("카트 아이템을 제거한다.")
    @Test
    void delete_cart_item() {
        // given
        Member member = createMember();
        List<CartItemResponse> beforeItems = cartService.findAllCartItems(member);

        // when
        cartService.remove(member, 1L);

        // then
        List<CartItemResponse> result = cartService.findAllCartItems(member);
        assertThat(result.size()).isEqualTo(beforeItems.size() - 1);
    }

    @DisplayName("모든 카트 아이템을 제거한다.")
    @Test
    void delete_all_cart_items() {
        // given
        Member member = createMember();

        // when
        cartService.deleteAllCartItems(member);

        // then
        List<CartItemResponse> result = cartService.findAllCartItems(member);
        assertThat(result.size()).isEqualTo(0);
    }
}
