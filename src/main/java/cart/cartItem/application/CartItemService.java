package cart.cartItem.application;

import cart.cartItem.application.dto.CartItemAddDto;
import cart.cartItem.application.dto.CartItemDto;
import cart.cartItem.application.dto.CartItemQuantityUpdateDto;
import cart.cartItem.domain.CartItem;
import cart.cartItem.persistence.CartItemDao;
import cart.common.exception.notFound.CartItemNotFountException;
import cart.common.exception.notFound.ProductNotFoundException;
import cart.member.domain.Member;
import cart.product.domain.Product;
import cart.product.persistence.ProductDao;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemDto> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemDto::from).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemAddDto cartItemAddDto) {
        Product product = productDao.findById(cartItemAddDto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        return cartItemDao.save(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateDto cartItemQuantityUpdateDto) {
        CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(CartItemNotFountException::new);
        cartItem.checkOwner(member);

        if (checkQuantityIsZero(cartItemQuantityUpdateDto)) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(cartItemQuantityUpdateDto.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public boolean checkQuantityIsZero(CartItemQuantityUpdateDto dto) {
        return dto.getQuantity() == 0;
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(CartItemNotFountException::new);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
