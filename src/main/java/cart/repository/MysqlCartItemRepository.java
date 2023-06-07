package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MysqlCartItemRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;
    private final ProductDao productDao;

    public MysqlCartItemRepository(CartItemDao cartItemDao, MemberDao memberDao, ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
    }


    public List<CartItem> findByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemEntity cartItemEntity : cartItemEntities) {
            ProductEntity productEntity = cartItemEntity.getProductEntity();
            MemberEntity memberEntity = cartItemEntity.getMemberEntity();
            Product product = new Product(productEntity.getId(), productEntity.getName(),
                    productEntity.getPrice(), productEntity.getImageUrl());
            Member member = new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
            cartItems.add(new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member));
        }
        return cartItems;
    }

    public Long save(CartItem cartItem) {
        Member member = cartItem.getMember();
        MemberEntity memberEntity = new MemberEntity(member.getId());
        Product product = cartItem.getProduct();
        ProductEntity productEntity = new ProductEntity(product.getId());
        return cartItemDao.save(new CartItemEntity(productEntity, memberEntity, cartItem.getId(), cartItem.getQuantity()));
    }

    public CartItem findById(Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id);
        ProductEntity productEntity = cartItemEntity.getProductEntity();
        MemberEntity memberEntity = cartItemEntity.getMemberEntity();
        Product product = new Product(productEntity.getId(), productEntity.getName(),
                productEntity.getPrice(), productEntity.getImageUrl());
        Member member = new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
    }


    public void delete(Long memberId, Long productId) {
        cartItemDao.delete(memberId, productId);
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    public void updateQuantity(CartItem cartItem) {
        CartItemEntity cartItemEntity = new CartItemEntity(cartItem.getId(), cartItem.getQuantity());
        cartItemDao.updateQuantity(cartItemEntity);
    }
}
