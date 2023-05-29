package cart.cart_item.application;

import cart.cart_item.application.dto.CartItemQuantityUpdateRequest;
import cart.cart_item.application.dto.CartItemRequest;
import cart.cart_item.application.dto.CartItemResponse;
import cart.cart_item.dao.CartItemDao;
import cart.cart_item.domain.CartItem;
import cart.member.domain.Member;
import cart.product.dao.ProductDao;
import java.util.List;
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
}
