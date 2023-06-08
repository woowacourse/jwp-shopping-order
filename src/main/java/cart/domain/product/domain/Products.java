package cart.domain.product.domain;

import java.util.ArrayList;
import java.util.List;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.cartitem.domain.CartItems;
import cart.domain.product.application.dto.ProductCartItemResponse;

public class Products {

    private final List<Product> products;

    public Products(final List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public List<ProductCartItemResponse> getProductCartItems(CartItems cartItems) {
        List<ProductCartItemResponse> productCartItemResponses = new ArrayList<>();
        for (Product product : products) {
            addProductByCartItemContainCase(cartItems, productCartItemResponses, product);
        }
        return productCartItemResponses;
    }

    private void addProductByCartItemContainCase(CartItems cartItems, List<ProductCartItemResponse> productCartItemResponses, Product product) {
        if (cartItems.isContainProduct(product)) {
            addContainsCartItemProductResponse(cartItems, productCartItemResponses, product);
        }
        if (!cartItems.isContainProduct(product)) {
            addOnlyProductResponse(productCartItemResponses, product);
        }
    }

    private void addContainsCartItemProductResponse(CartItems cartItems, List<ProductCartItemResponse> productCartItemResponses, Product product) {
        CartItem findCartItem = cartItems.findCartItemByProduct(product);
        ProductCartItemResponse productContainsCartItem = ProductCartItemResponse.createContainsCartItem(product, findCartItem);
        productCartItemResponses.add(productContainsCartItem);
    }

    private void addOnlyProductResponse(List<ProductCartItemResponse> productCartItemResponses, Product product) {
        ProductCartItemResponse productNotContainsCartItem = ProductCartItemResponse.createOnlyProduct(product);
        productCartItemResponses.add(productNotContainsCartItem);
    }

    public Boolean isContains(Product product) {
        return products.contains(product);
    }
}
