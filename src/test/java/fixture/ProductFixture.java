package fixture;

import cart.domain.Product;

public class ProductFixture {

    public static final int PRICE = 1_000_000_000;
    public static final Product HONG_HONG = new Product(1L, "홍홍", 1_000_000_000, "hognhong.com");
    public static final Product HONG_SILE = new Product(2L, "홍실", 1_000_000_000, "hongsil.com");

}
