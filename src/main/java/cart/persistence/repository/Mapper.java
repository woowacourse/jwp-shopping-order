package cart.persistence.repository;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;

class Mapper {

    public static Product productEntityToProductMapper(final ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageUrl()
        );
    }

    public static ProductEntity productToProductEntityMapper(final Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                null
        );
    }

    public static Member memberEntityToMember(final MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );
    }

    public static MemberEntity memberToMemberEntityMapper(final Member member) {
        return new MemberEntity(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getPoint(),
                null
        );
    }
}
