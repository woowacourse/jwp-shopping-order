package cart.application.service;

import cart.application.exception.CartItemNotFoundException;
import cart.application.exception.MemberNotFoundException;
import cart.application.exception.ProductNotFoundException;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.ProductRepository;
import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Product;
import cart.presentation.dto.request.AuthInfo;
import cart.presentation.dto.request.CartItemQuantityRequest;
import cart.presentation.dto.request.CartItemRequest;
import cart.presentation.dto.response.CartItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    @Transactional(readOnly = true)
    public List<CartItemResponse> findAllCartItems(AuthInfo authInfo) {
        Member member = memberRepository.findByEmail(authInfo.getEmail()) // TODO: AOP 적용?
                .orElseThrow(MemberNotFoundException::new);

        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long createCartItem(AuthInfo authInfo, CartItemRequest cartItemRequest) {
        Member member = memberRepository.findByEmail(authInfo.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        CartItem inserted = cartItemRepository.insert(new CartItem(null, 1, product, member));
        return inserted.getId();
    }

    public void updateQuantity(AuthInfo authInfo, Long id, CartItemQuantityRequest request) {
        Member member = memberRepository.findByEmail(authInfo.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(CartItemNotFoundException::new);
         // TODO: id + QuantityRequest 합치기
        cartItem.validateIsOwnedBy(member);
        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }
        cartItem.updateQuantity(request.getQuantity());
        cartItemRepository.update(cartItem);
    }

    public void remove(AuthInfo authInfo, Long id) {
        Member member = memberRepository.findByEmail(authInfo.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(CartItemNotFoundException::new);

        cartItem.validateIsOwnedBy(member);
        cartItemRepository.deleteById(id);
    }
}
