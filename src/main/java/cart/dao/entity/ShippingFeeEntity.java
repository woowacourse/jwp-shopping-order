package cart.dao.entity;

public class ShippingFeeEntity {
    private final Long id;
    private final Long fee;

    public ShippingFeeEntity(Long fee) {
        this(null, fee);
    }

    public ShippingFeeEntity(Long id, Long fee) {
        this.id = id;
        this.fee = fee;
    }

    public Long getId() {
        return id;
    }

    public Long getFee() {
        return fee;
    }
}
