package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CartItems {

    private static final int DELIVERY_FEE = 3_000;
    private static final int SALE_THRESHOLD = 50_000;
    private static final double SALE_RATE = 0.1;

    private final List<CartItem> cartItems;
    private final int totalPrice;
    private final int deliveryFee;
    private final MemberPoint savedPoint;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.totalPrice = calculateTotalPrice(cartItems);
        this.deliveryFee = calculateDeliveryFee(totalPrice);
        this.savedPoint = new MemberPoint(calculateSavedPoint(totalPrice));
    }

    private int calculateSavedPoint(final int price) {
        return (int) (price * SALE_RATE);
    }

    private int calculateTotalPrice(final List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(cartitem -> cartitem.getProductPrice() * cartitem.getQuantityValue())
                .sum();
    }

    private int calculateDeliveryFee(final int totalPrice) {
        if (totalPrice >= SALE_THRESHOLD) {
            return 0;
        }
        return DELIVERY_FEE;
    }

    public void checkOwner(final Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    public List<OrderProduct> toOrderProducts(final Order order, final List<Product> products) {
        return IntStream.range(0, products.size())
                .mapToObj(index -> new OrderProduct(
                        order,
                        products.get(index).getId(),
                        products.get(index).getProductName(),
                        products.get(index).getProductPrice(),
                        products.get(index).getProductImageUrl(),
                        cartItems.get(index).getQuantity())
                ).collect(Collectors.toList());
    }

    public List<Long> getProductIds() {
        return cartItems.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());
    }

    public List<Long> getCartItemIds() {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
    }

    public Optional<CartItem> findProduct(final Product product) {
        return cartItems.stream()
                .filter(m -> m.getProduct().equals(product))
                .findFirst();
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public MemberPoint getSavedPoint() {
        return savedPoint;
    }
}
