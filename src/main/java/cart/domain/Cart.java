package cart.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cart {
    
    private final List<CartItem> cartItems;
    
    public Cart(List<CartItem> cartItems) {
        this.cartItems = new ArrayList<>(cartItems);
    }
    
    public List<OrderItem> createOrderItems(List<CartItem> itemsToOrder) {
        List<CartItem> orderedItem = deleteCartItems(itemsToOrder);
        cartItems.removeAll(orderedItem);
        return orderedItem.stream()
                .map(itemToOrder ->
                        new OrderItem(
                                null,
                                itemToOrder.getMember().getId(),
                                itemToOrder.getProduct(),
                                itemToOrder.getQuantity()
                        ))
                .collect(Collectors.toList());
    }
    
    private List<CartItem> deleteCartItems(List<CartItem> itemsToOrder) {
        List<CartItem> orderedItems = new ArrayList<>();
        for (CartItem itemToOrder : itemsToOrder) {
            CartItem productInCart = cartItems.stream()
                    .peek(cartItem -> cartItem.checkOwner(itemToOrder.getMember()))
                    .filter(cartItem -> cartItem.getProduct().equals(itemToOrder.getProduct()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("주문하려는 상품이 장바구니에 존재하지 않습니다."));
            if (productInCart.getQuantity() != itemToOrder.getQuantity()) {
                throw new IllegalArgumentException("주문하려는 상품의 수량이 장바구니에 담긴 상품의 수량과 일치하지 않습니다.");
            }
            orderedItems.add(productInCart);
        }
        return orderedItems;
    }
    
    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
