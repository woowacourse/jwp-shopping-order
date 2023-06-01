package cart.dto;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.DiscountType;
import cart.dto.apidatamapper.DiscountAmountMapper;
import cart.dto.apidatamapper.DiscountTypeMapper;

public class CouponResponse {

    private Long id;
    private String name;
    private String type;
    private Integer amount;

    public CouponResponse() {
    }

    public CouponResponse(Long id, String name, String type, Integer amount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.amount = amount;
    }

    public static CouponResponse of(Coupon coupon, DiscountType discountType) {
        String type = DiscountTypeMapper.domainToApiBodyString(discountType);
        int amount = DiscountAmountMapper.domainValueToApiBodyAmount(discountType, coupon.getDiscountValue());

        return new CouponResponse(coupon.getId(), coupon.getName(), type, amount);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }
}
