package cart.domain;

import cart.domain.coupon.Coupon;
import cart.domain.disount.NoDiscountPolicy;
import cart.domain.disount.DiscountPolicy;
import cart.domain.disount.PriceDiscountPolicy;
import cart.domain.value.Price;

public class Product {
    private Long id;
    private String name;
    private Price price;
    private String imageUrl;
    private DiscountPolicy discountPolicy;

    public Product(Long id, String name, int price, String imageUrl, DiscountPolicy discountPolicy) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.imageUrl = imageUrl;
        this.discountPolicy = discountPolicy;
    }


    // TODO: 2023/06/01 확장성 or 고정
    public Product(Long id, String name, int price, String imageUrl, int discountPrice) {
        this(id, name, price, imageUrl, new PriceDiscountPolicy(discountPrice));
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this(id, name, price, imageUrl, new NoDiscountPolicy());
    }

    public Product(String name, int price, String imageUrl, int discountPrice) {
        this(null, name, price, imageUrl, new PriceDiscountPolicy(discountPrice));
    }

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl, new NoDiscountPolicy());
    }

    public Price getDiscountedPrice() {
        if (isOnSale()) {
            return discountPolicy.discount(price);
        }
        return price;
    }

    public boolean isOnSale() {
        return !price.equals(discountPolicy.discount(price));
    }

    public Price applyCoupon(Coupon coupon) {
        return coupon.apply(getDiscountedPrice());
    }

    public Price getDiscountPrice() {
        return price.minus(getDiscountedPrice());
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

}
