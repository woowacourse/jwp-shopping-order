package cart.domain;

import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private PriceVO price;
    private String imageUrl;
    private StockVO stock;


    public Product(String name, int price, String imageUrl, int stock) {
        this(null, name, price, imageUrl, stock);
    }

    public Product(Long id, String name, int price, String imageUrl, int stock) {
        this.id = id;
        this.name = name;
        this.price = new PriceVO(price);
        this.imageUrl = imageUrl;
        this.stock = new StockVO(stock);
    }

    public void useStock(int stock) {
        this.stock = new StockVO(getStock() - stock);
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
        return stock.getStock();
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
