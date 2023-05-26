package cart.cartitem.application;

import cart.cartitem.application.dto.AddCartItemCommand;
import cart.cartitem.application.dto.UpdateCartItemQuantityCommand;
import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.cartitem.presentation.dto.CartItemResponse;
import cart.member.domain.Member;
import cart.product.domain.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public Long add(AddCartItemCommand command) {
        return cartItemRepository.save(new CartItem(
                        productRepository.findById(command.getProductId()),
                        command.getMember()
                )
        );
    }

    public void updateQuantity(UpdateCartItemQuantityCommand command) {
        CartItem cartItem = cartItemRepository.findById(command.getId());
        cartItem.checkOwner(command.getMember());

        if (command.getQuantity() == 0) {
            cartItemRepository.deleteById(command.getId());
            return;
        }

        cartItem.changeQuantity(command.getQuantity());
        cartItemRepository.update(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);
        cartItemRepository.deleteById(id);
    }
}
