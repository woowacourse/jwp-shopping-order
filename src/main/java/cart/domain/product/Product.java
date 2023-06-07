package cart.domain.product;

import java.util.Objects;

public class Product {

    private Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final SalePrice salePrice;

    public Product(final String name, final int price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.salePrice = SalePrice.createDefault();
    }

    public Product(final Long id, final String name, final int price, final String imageUrl, final Integer salePrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.salePrice = SalePrice.from(salePrice);
    }

    public int applySale(final int percentage) {
        return salePrice.applySale(price, percentage);
    }

    public void unApplySale() {
        salePrice.unApplySale();
    }

    public int getAppliedDiscountOrOriginPrice() {
        if (salePrice.isNoneSale()) {
            return price;
        }

        return price - salePrice.getSalePrice();
    }

    public boolean isSame(final Long id) {
        return Objects.equals(this.id, id);
    }

    public int getSalePrice() {
        return salePrice.getSalePrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(final Object o) {
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
