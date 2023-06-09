package cart.factory;

import cart.domain.Amount;
import cart.domain.Product;

public class ProductFactory {

  public static Product createProduct(final long id, final String name, final int price, final String imageUrl) {
    return new Product(id, name, new Amount(price), imageUrl);
  }

  public static Product createProduct(final String name, final int price, final String imageUrl) {
    return new Product(name, new Amount(price), imageUrl);
  }
}
