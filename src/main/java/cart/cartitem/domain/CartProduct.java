package cart.cartitem.domain;

import cart.product.domain.Product;
import java.util.Objects;

public class CartProduct {

    private final String name;
    private final String imageUrl;
    private final int price;

    private CartProduct(String name, String imageUrl, int price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public static CartProduct from(Product product) {
        return new CartProduct(product.getName(),
                product.getImageUrl(),
                product.getPrice());
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartProduct that = (CartProduct) o;
        return price == that.price && Objects.equals(name, that.name) && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, imageUrl, price);
    }
}
