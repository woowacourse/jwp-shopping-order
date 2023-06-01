package cart.Repository.mapper;

import cart.domain.CartItem;
import cart.domain.Product.Product;
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

    public List<CartItem> toCartItems(List<CartItemEntity> cartItemEntities, List<ProductEntity> productEntities, MemberEntity memberEntity) {
        Map<Long, Product> productMappingById = productMapper.productMappingById(productEntities);

        return cartItemEntities.stream()
                .map(it -> toCartItem(it, productMappingById.get(it.getProductId()), memberEntity))
                .collect(toList());
    }
//
//    private CartItem toCartItemFrom(CartItemEntity cartItemEntity, Map<Long, ProductEntity> productsInCart, MemberEntity memberEntity) {
//        ProductEntity productEntity = productsInCart.get(cartItemEntity.getProductId());
//
//        return toCartItem(cartItemEntity, productEntity, memberEntity);
//    }

    public CartItem toCartItem(CartItemEntity cartItemEntity, ProductEntity productEntity, MemberEntity memberEntity) {
        Product product = productMapper.toProduct(productEntity);
        return toCartItem(cartItemEntity, product, memberEntity);
    }

    public CartItem toCartItem(CartItemEntity cartItemEntity, Product product, MemberEntity memberEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                product,
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
