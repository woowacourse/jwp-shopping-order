package cart.domain;

public class Coupon {

    private final Long id;
    private final String name;
    private final String description;
    private final int discountAmount;
    private final Member member;

    public Coupon(final Long id, final String name, final String description, final int discountAmount, final Member member) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.discountAmount = discountAmount;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public Member getMember() {
        return member;
    }
}
