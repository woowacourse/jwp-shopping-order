package cart.domain;

public class Coupon {
    private final Long id;
    private final String name;
    private final Percent discountPercent;

    public Coupon(Long id, String name, int discountPercent) {
        validatePercent(discountPercent);
        this.id = id;
        this.name = name;
        this.discountPercent = new Percent(discountPercent);
    }

    public Coupon(String name, int discountPercent) {
        this(null, name, discountPercent);
    }

    private void validatePercent(int discountPercent) {
        if (discountPercent == 0) {
            throw new IllegalArgumentException("쿠폰의 할인퍼센트는 0퍼센트가 될 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Percent getDiscountPercent() {
        return discountPercent;
    }
}
