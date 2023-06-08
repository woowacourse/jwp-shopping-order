package cart.persistence.mapper;

import static cart.persistence.mapper.ProductMapper.convertProduct;

import cart.application.dto.order.OrderProductRequest;
import cart.domain.cartitem.Cart;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.persistence.dao.dto.CartItemDto;
import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static Cart convertCart(final CartItemDto cartItemDto) {
        final Member member = MemberMapper.convertMember(cartItemDto);
        final CartItem cartItem = convertCartItem(cartItemDto);
        return new Cart(member, List.of(cartItem));
    }

    public static Cart convertCart(final List<CartItemDto> carItems, final MemberEntity memberEntity) {
        final Member member = MemberMapper.convertMember(memberEntity);
        final List<CartItem> cartItems = carItems.stream()
            .map(CartMapper::convertCartItem)
            .collect(Collectors.toUnmodifiableList());
        return new Cart(member, cartItems);
    }

    public static List<CartItem> convertCartItems(final List<OrderDto> orderDtos) {
        return orderDtos.stream()
            .map(CartMapper::convertCartItem)
            .collect(Collectors.toUnmodifiableList());
    }

    public static CartItem convertCartItem(final CartItem cartItem, final OrderProductRequest orderProductRequest) {
        return new CartItem(cartItem.getCartId(),
            orderProductRequest.getQuantity(), cartItem.getProduct());
    }

    private static CartItem convertCartItem(final CartItemDto cartItemDto) {
        return new CartItem(cartItemDto.getCartId(), cartItemDto.getProductQuantity(),
            convertProduct(cartItemDto));
    }

    private static CartItem convertCartItem(final OrderDto orderDto) {
        final Product Product = convertProduct(orderDto);
        return new CartItem(orderDto.getOrderQuantity(), Product);
    }
}
