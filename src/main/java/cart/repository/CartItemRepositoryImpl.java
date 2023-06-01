package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.repository.CartItemRepository;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import cart.exception.CartItemException;
import cart.exception.ProductException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemRepositoryImpl implements CartItemRepository {
    private final ProductDao productDao;
    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;

    public CartItemRepositoryImpl(ProductDao productDao, MemberDao memberDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
    }

    @Override
    public List<CartItem> findByMemberId(Long id) {

        return cartItemDao.findByMemberId(id).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Long save(CartItem cartItem) {
        return cartItemDao.save(toEntity(cartItem));
    }

    @Override
    public CartItem findById(Long id) {
        return toDomain(cartItemDao.findById(id)
                .orElseThrow(()->new CartItemException("존재하지 않는 장바구니 상품입니다.")));
    }

    @Override
    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(toEntity(cartItem));
    }

    @Override
    public List<CartItem> findAllByIds(Member member, List<Long> cartProductIds) {
        return cartItemDao.findAllByIds(member.getId(), cartProductIds).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    private CartItemEntity toEntity(CartItem cartItem) {
        return new CartItemEntity(cartItem.getId(), cartItem.getMember().getId(),
                cartItem.getProduct().getId(), cartItem.getQuantity());
    }

    private CartItem toDomain(CartItemEntity entity) {
        ProductEntity productEntity = productDao.getProductById(entity.getProductId())
                .orElseThrow(()->new ProductException("잘못된 상품 입니다."));

        Product product = new Product(productEntity.getId(), productEntity.getName(),
                productEntity.getPrice(), productEntity.getImageUrl());

        Member member = memberDao.getMemberById(entity.getMemberId());

        return new CartItem(entity.getId(), entity.getQuantity(), product, member);
    }
}
