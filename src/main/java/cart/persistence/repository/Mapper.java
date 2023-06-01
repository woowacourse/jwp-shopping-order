package cart.persistence.repository;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;

class Mapper {

    public static Product productMapper(final ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageUrl()
        );
    }

    public static ProductEntity productEntityMapper(final Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                null
        );
    }

    public static Member memberMapper(final MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );
    }

    public static MemberEntity memberEntityMapper(final Member member) {
        return new MemberEntity(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getPoint(),
                null
        );
    }

    public static CartItem cartItemMapper(final CartItemEntity cartItemEntity, final MemberEntity memberEntity, final ProductEntity productEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                memberMapper(memberEntity),
                productMapper(productEntity),
                cartItemEntity.getQuantity()
        );
    }

    public static CartItemEntity cartItemEntityMapper(final CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember().getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity(),
                null
        );
    }
}
