package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.CostResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //TODO: 여기서 개발 이어서~~
    public CostResponse getCosts(Long memberId) {
        int totalItemPrice = findTotalItemPrice(memberId);
        int discountedTotalItemPrice = findDiscountedTotalItemPrice(memberId);
        int shippingFee = findShippingFee(totalItemPrice);
        int totalPrice = discountedTotalItemPrice + shippingFee;
        int totalItemDiscountAmount = findTotalItemDiscountAmount(memberId);
        int totalMemberDiscountAmount = findTotalMemberDiscountAmount(memberId);

        return new CostResponse(totalItemDiscountAmount, totalMemberDiscountAmount, totalItemPrice, discountedTotalItemPrice, totalPrice);
    }

    private int findTotalMemberDiscountAmount(Long memberId) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId);
        List<Integer> discountedPrice = new ArrayList<>();
        int discountedPercentage = memberDao.getMemberById(memberId).findDiscountedPercentage();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int price = product.getPrice() - product.calculateMemberDiscountedPrice(discountedPercentage);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

    private int findTotalItemPrice(Long memberId) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId);
        List<Integer> prices = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            prices.add(cartItem.getProduct().getPrice());
        }

        return Order.calculatePriceSum(prices);
    }

    private int findDiscountedTotalItemPrice(Long memberId) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId);
        List<Integer> discountedPrice = new ArrayList<>();
        int memberDiscount = memberDao.getMemberById(memberId).findDiscountedPercentage();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int price = product.calculateDiscountedPrice(memberDiscount);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

    private int findShippingFee(int totalItemPrice) {
        if (totalItemPrice >= 50000) {
            return 0;
        }
        return 3000;
    }

    private int findTotalItemDiscountAmount(Long memberId) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId);
        List<Integer> discountedPrice = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int price = product.getPrice() - product.calculateDiscountedPrice();
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

}
