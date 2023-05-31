package cart.domain;

import cart.exception.OrderException;

import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private PriceVO price;
    private String imageUrl;
    private int stock;


    public Product(String name, int price, String imageUrl, int stock) {
        this(null, name, price, imageUrl, stock);
    }

    public Product(Long id, String name, int price, String imageUrl, int stock) {
        validateStock(stock);
        this.id = id;
        this.name = name;
        this.price = new PriceVO(price);
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    private void validateStock(int stock) {
        if (stock < 0) {
            throw new OrderException.NotEnoughStockException(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price.getPrice();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
