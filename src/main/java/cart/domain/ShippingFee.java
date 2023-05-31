package cart.domain;

public enum ShippingFee {
    BASIC(3000),
    NONE(0);

    public static final int FREE_DELIVERY_CRITERIA = 30000;
    private final Integer charge;

    ShippingFee(final Integer charge) {
        this.charge = charge;
    }
    
    public static ShippingFee from(final Integer price) {
        if (price > FREE_DELIVERY_CRITERIA) {
            return NONE;
        }
        return BASIC;
    }

    public Integer getCharge() {
        return charge;
    }
}
