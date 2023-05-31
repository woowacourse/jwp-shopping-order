package cart.cart_item.application;

import cart.cart_item.application.dto.CartItemQuantityUpdateRequest;
import cart.cart_item.application.dto.CartItemRequest;
import cart.cart_item.application.dto.CartItemResponse;
import cart.cart_item.application.dto.RemoveCartItemRequest;
import cart.cart_item.dao.CartItemDao;
import cart.cart_item.domain.CartItem;
import cart.cart_item.exception.CanNotRemoveCartItemsMoreThanSavedCartItems;
import cart.cart_item.exception.CanNotRemoveNotMyCartItemException;
import cart.member.domain.Member;
import cart.product.dao.ProductDao;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

  private final ProductDao productDao;
  private final CartItemDao cartItemDao;

  public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
    this.productDao = productDao;
    this.cartItemDao = cartItemDao;
  }

  public List<CartItemResponse> findByMember(Member member) {
    List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
    return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
  }

  public List<CartItem> findCartItemByCartIds(final List<Long> cartItemIds, final Member member) {
    return cartItemDao.findByIdsIn(cartItemIds, member.getId());
  }

  public Long add(Member member, CartItemRequest cartItemRequest) {
    return cartItemDao.save(
        new CartItem(member, productDao.getProductById(cartItemRequest.getProductId())));
  }

  public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
    CartItem cartItem = cartItemDao.findById(id);
    cartItem.checkOwner(member);

    if (request.getQuantity() == 0) {
      cartItemDao.deleteById(id);
      return;
    }

    cartItem.changeQuantity(request.getQuantity());
    cartItemDao.updateQuantity(cartItem);
  }

  public void remove(Member member, Long id) {
    CartItem cartItem = cartItemDao.findById(id);
    cartItem.checkOwner(member);

    cartItemDao.deleteById(id);
  }

  public void removeBatch(final Member member, final RemoveCartItemRequest removeCartItemRequest) {

    final Set<Long> savedCartItemIds = cartItemDao.findByMemberId(member.getId())
        .stream()
        .map(CartItem::getId)
        .collect(Collectors.toSet());

    final List<Long> deleteCartItemIds = removeCartItemRequest.getCartItemIds();

    validateSizeSavedAndDeleteCartItem(savedCartItemIds, deleteCartItemIds);
    validateSavedCartItemContainsDeleteCartItem(savedCartItemIds, deleteCartItemIds);

    cartItemDao.batchDeleteByIdsIn(removeCartItemRequest.getCartItemIds(), member.getId());
  }

  private void validateSavedCartItemContainsDeleteCartItem(
      final Set<Long> savedCartItemIds,
      final List<Long> deleteCartItemIds
  ) {
    if (!savedCartItemIds.containsAll(deleteCartItemIds)) {
      throw new CanNotRemoveNotMyCartItemException("자기 자신의 카트에 들어있는 물품만 삭제 가능합니다.");
    }
  }

  private void validateSizeSavedAndDeleteCartItem(
      final Set<Long> savedCartItemIds,
      final List<Long> deleteCartItemIds
  ) {
    if (deleteCartItemIds.size() > savedCartItemIds.size()) {
      throw new CanNotRemoveCartItemsMoreThanSavedCartItems
          ("삭제할 카트 물품의 개수가 현재 저장된 카트 물품의 개수보다 많을 수는 없습니다.");
    }
  }
}
