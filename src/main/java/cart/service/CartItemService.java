package cart.service;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.MemberRepository;
import cart.domain.repository.ProductRepository;
import cart.dto.AuthMember;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import cart.exception.ExceptionType;
import cart.exception.MemberException;
import cart.exception.ProductException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(MemberRepository memberRepository, ProductRepository productRepository,
                           CartItemRepository cartItemRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findBy(AuthMember member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long addCart(AuthMember authMember, CartItemRequest cartItemRequest) {
        Member member = toMember(authMember);
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ProductException(ExceptionType.NOT_FOUND_PRODUCT));
        CartItem cartItem = new CartItem(product, member);
        return cartItemRepository.save(cartItem).getId();
    }

    private Member toMember(AuthMember authMember) {
        return memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new MemberException(ExceptionType.NOT_FOUND_MEMBER));
    }

    public void updateQuantity(AuthMember authMember, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemException(ExceptionType.NOT_FOUND_CART_ITEM));
        Member member = toMember(authMember);
        cartItem.validateOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(AuthMember authMember, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemException(ExceptionType.NOT_FOUND_CART_ITEM));
        Member member = toMember(authMember);
        cartItem.validateOwner(member);

        cartItemRepository.deleteById(id);
    }
}
