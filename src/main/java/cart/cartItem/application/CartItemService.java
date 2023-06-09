package cart.cartItem.application;

import cart.cartItem.application.dto.CartItemAddDto;
import cart.cartItem.application.dto.CartItemDto;
import cart.cartItem.application.dto.CartItemQuantityUpdateDto;
import cart.cartItem.domain.CartItem;
import cart.common.notFoundException.CartItemNotFountException;
import cart.common.notFoundException.ProductNotFoundException;
import cart.member.domain.Member;
import cart.product.application.ProductRepository;
import cart.product.domain.Product;
import cart.product.persistence.ProductDao;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemDto> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemDto::from).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemAddDto cartItemAddDto) {
        Product product = productRepository.findById(cartItemAddDto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        return cartItemRepository.save(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateDto cartItemQuantityUpdateDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(CartItemNotFountException::new);
        cartItem.checkOwner(member);

        if (checkQuantityIsZero(cartItemQuantityUpdateDto)) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(cartItemQuantityUpdateDto.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public boolean checkQuantityIsZero(CartItemQuantityUpdateDto dto) {
        return dto.getQuantity() == 0;
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(CartItemNotFountException::new);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }
}
