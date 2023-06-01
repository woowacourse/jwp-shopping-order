package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Price;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.CostResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao, MemberDao memberDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemDao.save(new CartItem(member, productDao.getProductById(cartItemRequest.getProductId()), cartItemRequest.getQuantity()));
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

    public CostResponse getCosts(Long memberId) {
        int memberDiscount = memberDao.getMemberById(memberId).findDiscountedPercentage();

        int totalItemPrice = Price.calculateTotalItemPrice(cartItemDao.findByMemberId(memberId));
        int discountedTotalItemPrice = Price.calculateDiscountedTotalItemPrice(cartItemDao.findByMemberId(memberId), memberDiscount);
        int shippingFee = Price.calculateShippingFee(totalItemPrice);
        int totalPrice = discountedTotalItemPrice + shippingFee;
        int totalItemDiscountAmount = Price.calculateTotalItemDiscountAmount(cartItemDao.findByMemberId(memberId));
        int totalMemberDiscountAmount = Price.calculateTotalMemberDiscountAmount(cartItemDao.findByMemberId(memberId), memberDiscount);

        return new CostResponse(totalItemDiscountAmount, totalMemberDiscountAmount, totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice);
    }
}
