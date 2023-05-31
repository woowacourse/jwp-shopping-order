package cart.domain;

public class Product {
    private Long id;
    private String name;
    private Price price;
    private String imageUrl;
    private Percent discountPercent;

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl, 0);
    }

    public Product(String name, int price, String imageUrl, int discountPercent) {
        this(null, name, price, imageUrl, discountPercent);
    }

    public Product(Long id, String name, int price, String imageUrl, int discountPercent) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.imageUrl = imageUrl;
        this.discountPercent = new Percent(discountPercent);
    }

    public Price gerPriceOnSale() {
        if (isOnSale()) {
            return price.discount(discountPercent);
        }
        return price;
    }

    public boolean isOnSale() {
        return !discountPercent.isZero();
    }

    public Price applyCoupon(Coupon coupon) {
        return coupon.apply(gerPriceOnSale());
    }

    public Price getDiscountPrice() {
        return price.minus(gerPriceOnSale());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Percent getDiscountPercent() {
        return discountPercent;
    }
}
