package cart.persistence.repository;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;
import cart.persistence.entity.*;

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
                product.getImageUrl()
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
                member.getPoint()
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
                cartItem.getQuantity()
        );
    }

    public static OrderHistoryEntity orderHistoryEntityMapper(final Order order) {
        return new OrderHistoryEntity(
                order.getId(),
                order.getMember().getId(),
                order.getTotalAmount(),
                order.getUsedPoint(),
                order.getSavedPoint()
        );
    }

    public static OrderProductEntity orderProductEntityMapper(final OrderProduct orderProduct, final Long orderId) {
        return new OrderProductEntity(
                orderProduct.getId(),
                orderId,
                orderProduct.getProduct().getId(),
                orderProduct.getProduct().getName(),
                orderProduct.getProduct().getImageUrl(),
                orderProduct.getPurchasedPrice(),
                orderProduct.getQuantity()
        );
    }
}
