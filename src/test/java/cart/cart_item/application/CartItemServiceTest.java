package cart.cart_item.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cart.cart_item.application.dto.RemoveCartItemRequest;
import cart.cart_item.domain.CartItem;
import cart.cart_item.exception.CanNotRemoveCartItemsMoreThanSavedCartItems;
import cart.cart_item.exception.NotExistCartItemInCart;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
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

  @Test
  @DisplayName("removeBatch() : 삭제할 카트 물픔 개수가 저장된 카트 물품 개수보다 많으면 CanNotRemoveCartItemsMoreThanSavedCartItems가 발생한다.")
  void test_removeBatch_CanNotRemoveCartItemsMoreThanSavedCartItems() throws Exception {
    //given
    //member 1이 가지고 있는 cartItem [1, 2, 3, 4, 5, 6]
    //member 2가 가지고 있는 cartItem [1, 2, 7]
    final Member member = memberDao.getMemberById(2L);

    final List<Long> deleteCartItemIds = List.of(1L, 2L, 7L);

    final RemoveCartItemRequest removeCartItemRequest =
        new RemoveCartItemRequest(deleteCartItemIds);

    //when & then
    assertThatThrownBy(() -> cartItemService.removeBatch(member, removeCartItemRequest))
        .isInstanceOf(CanNotRemoveCartItemsMoreThanSavedCartItems.class);
  }

  @Test
  @DisplayName("removeBatch() : 삭제할 카트 품목이 카트에 존재하지 않는다면 NotExistCartItemInCart 발생한다.")
  void test_removeBatch_NotExistCartItemInCart() throws Exception {
    //given
    //member 1이 가지고 있는 cartItem [1, 2, 3, 4, 5, 6]
    //member 2가 가지고 있는 cartItem [1, 2, 7]
    final Member member = memberDao.getMemberById(1L);

    final List<Long> deleteCartItemIds = List.of(1L, 2L, 7L);

    final RemoveCartItemRequest removeCartItemRequest =
        new RemoveCartItemRequest(deleteCartItemIds);

    //when & then
    assertThatThrownBy(() -> cartItemService.removeBatch(member, removeCartItemRequest))
        .isInstanceOf(NotExistCartItemInCart.class);
  }
}
