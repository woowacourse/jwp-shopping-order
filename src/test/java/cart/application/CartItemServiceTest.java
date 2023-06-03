package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.exception.notfound.MemberNotFoundException;
import cart.ui.dto.cartitem.CartItemRequest;
import cart.ui.dto.order.CartItemsPriceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class CartItemServiceTest {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CartItemDao cartItemDao;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);
    }

    @DisplayName("장바구니에 상품을 추가할 때, 이미 해당 상품이 존재하면 수량만 하나 추가한다.")
    @Test
    void addCartItem() {
        // given
        final CartItemRequest cartItemRequest = new CartItemRequest(1L);

        // when
        cartItemService.addCartItem(member, cartItemRequest);
        final CartItem cartItem = cartItemDao.findById(1L).get();
        final List<CartItem> cartItems = cartItemDao.findAllByMemberId(member.getId());

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(new Quantity(3));
        assertThat(cartItems).hasSize(6);
    }

    @DisplayName("장바구니 상품에 대한 총 가격과 배송비를 구한다.")
    @Test
    void getPaymentInfo1() {
        // given
        final List<Long> cartItemIds = List.of(1L, 2L, 3L);

        // when
        final CartItemsPriceResponse response = cartItemService.getPaymentInfo(member, cartItemIds);

        // then
        assertAll(
                () -> assertThat(response.getTotalPrice()).isEqualTo(120_000),
                () -> assertThat(response.getDeliveryFee()).isEqualTo(0)
        );
    }

    @DisplayName("장바구니 상품에 대한 총 가격과 배송비를 구한다.")
    @Test
    void getPaymentInfo2() {
        // given
        final List<Long> cartItemIds = List.of(1L);

        // when
        final CartItemsPriceResponse response = cartItemService.getPaymentInfo(member, cartItemIds);

        // then
        assertAll(
                () -> assertThat(response.getTotalPrice()).isEqualTo(20_000),
                () -> assertThat(response.getDeliveryFee()).isEqualTo(3000)
        );
    }
}
