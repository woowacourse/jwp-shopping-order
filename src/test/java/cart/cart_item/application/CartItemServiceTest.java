package cart.cart_item.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cart.cart_item.domain.CartItem;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CartItemServiceTest {

  @Autowired
  private CartItemService cartItemService;

  @Autowired
  private MemberDao memberDao;

  @Test
  @DisplayName("findCartItemByCartIds() : id에 포함되어있는 cart item을 모두 조회할 수 있다.")
  void test_findCartItemByCartIds() throws Exception {
    //given
    final List<Long> cartItemIds = List.of(1L, 2L, 4L);
    final Member member = memberDao.getMemberById(1L);

    //when
    final List<CartItem> cartItems = cartItemService.findCartItemByCartIds(cartItemIds,
        member);

    //then
    final List<Long> savedCartItemIds = cartItems.stream()
        .map(CartItem::getId)
        .collect(Collectors.toList());

    Assertions.assertAll(
        () -> assertEquals(3, savedCartItemIds.size()),
        () -> assertThat(savedCartItemIds).containsAnyElementsOf(List.of(1L, 2L, 3L))
    );
  }
}
