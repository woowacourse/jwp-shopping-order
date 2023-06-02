package cart.Repository.mapper;

import cart.domain.CartItem;
import cart.domain.Product.Product;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;

import java.util.List;
import java.util.Map;

import static cart.Repository.mapper.MemberMapper.toMember;
import static cart.Repository.mapper.ProductMapper.productMappingById;
import static cart.Repository.mapper.ProductMapper.toProduct;
import static java.util.stream.Collectors.toList;

public class CartItemMapper {

    public static List<CartItem> toCartItems(List<CartItemEntity> cartItemEntities, List<ProductEntity> productEntities, MemberEntity memberEntity) {
        Map<Long, Product> productMappingById = productMappingById(productEntities);

        return cartItemEntities.stream()
                .map(it -> toCartItem(it, productMappingById.get(it.getProductId()), memberEntity))
                .collect(toList());
    }

    public static CartItem toCartItem(CartItemEntity cartItemEntity, ProductEntity productEntity, MemberEntity memberEntity) {
        Product product = toProduct(productEntity);
        return toCartItem(cartItemEntity, product, memberEntity);
    }

    public static CartItem toCartItem(CartItemEntity cartItemEntity, Product product, MemberEntity memberEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                product,
                toMember(memberEntity)
        );
    }

    public static CartItemEntity toCartItemEntity(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getProduct().getId(),
                cartItem.getMember().getId()
        );
    }
}
