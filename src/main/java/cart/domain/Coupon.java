package cart.domain;

public class Coupon {

    private Long id;
    private String name;
    private Discount discount;

    public Coupon(final Long id, final String name, final Discount discount) {
        this.id = id;
        this.name = name;
        this.discount = discount;
    }

    public Coupon(final String name, final Discount discount) {
        this.name = name;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Discount getDiscount() {
        return discount;
    }
}
