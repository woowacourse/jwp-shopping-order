package cart.domain.order;

public class ShippingFee {

    public static final int BASE_SHIPPING_FEE = 3_000;
    public static final int FREE_SHIPPING_AMOUNT = 30_000;

    private final int value;

    private ShippingFee(final int value) {
        this.value = value;
    }

    public static ShippingFee fromTotalOrderAmount(final int totalOrderAmount) {
        if (FREE_SHIPPING_AMOUNT <= totalOrderAmount) {
            return new ShippingFee(0);
        }
        return new ShippingFee(BASE_SHIPPING_FEE);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ShippingFee that = (ShippingFee) o;

        if (value != that.value) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }

    public int getValue() {
        return value;
    }
}
