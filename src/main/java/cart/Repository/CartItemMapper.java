package cart.Repository;

import cart.domain.CartItem;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
public class CartItemMapper {
    private final ProductMapper productMapper;
    private final MemberMapper memberMapper;


    public CartItemMapper(ProductMapper productMapper, MemberMapper memberMapper) {
        this.productMapper = productMapper;
        this.memberMapper = memberMapper;
    }

    public List<CartItem> toCartItems(List<CartItemEntity> cartItemEntities, Map<Long, ProductEntity> productsInCart, MemberEntity memberEntity) {
        return cartItemEntities.stream()
                .map(it -> toCartItemFrom(it, productsInCart, memberEntity))
                .collect(toList());
    }

    private CartItem toCartItemFrom(CartItemEntity cartItemEntity, Map<Long, ProductEntity> productsInCart, MemberEntity memberEntity) {
        ProductEntity productEntity = productsInCart.get(cartItemEntity.getProductId());

        return toCartItem(cartItemEntity, productEntity, memberEntity);
    }

    public CartItem toCartItem(CartItemEntity cartItemEntity, ProductEntity productEntity, MemberEntity memberEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                productMapper.toProduct(productEntity),
                memberMapper.toMember(memberEntity)
        );
    }

    public CartItemEntity toCartItemEntity(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getProduct().getId(),
                cartItem.getMember().getId()
        );
    }
}
