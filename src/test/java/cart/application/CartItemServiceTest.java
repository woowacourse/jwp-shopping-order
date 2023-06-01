package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.dto.CartItemRequest;
import cart.exception.notfound.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CartItemServiceTest {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CartItemDao cartItemDao;

    @DisplayName("장바구니에 상품을 추가할 때, 이미 해당 상품이 존재하면 수량만 하나 추가한다.")
    @Test
    void addCartItem() {
        // given
        final Member member = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);
        final CartItemRequest cartItemRequest = new CartItemRequest(1L);

        // when
        cartItemService.addCartItem(member, cartItemRequest);
        final CartItem cartItem = cartItemDao.findById(1L).get();
        final List<CartItem> cartItems = cartItemDao.findAllByMemberId(member.getId());

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(new Quantity(3));
        assertThat(cartItems).hasSize(6);
    }
}
