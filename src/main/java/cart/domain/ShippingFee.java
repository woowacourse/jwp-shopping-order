package cart.domain;

import cart.dao.entity.ShippingFeeEntity;

public class ShippingFee {

    private final Long id;
    private final Long fee;

    public ShippingFee(Long fee) {
        this(null, fee);
    }

    public ShippingFee(Long id, Long fee) {
        this.id = id;
        this.fee = fee;
    }

    public static ShippingFee from(ShippingFeeEntity shippingFeeEntity) {
        return new ShippingFee(shippingFeeEntity.getId(), shippingFeeEntity.getFee());
    }

    public Long getId() {
        return id;
    }

    public Long getFee() {
        return fee;
    }
}
