package cart.service.dto;

import cart.auth.Credentials;

public class CouponSaveDto {

    private final long id;
    private final String email;
    private final String password;
    private final long couponId;

    public CouponSaveDto(final long id, final String email, final String password, final long couponId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.couponId = couponId;
    }

    public static CouponSaveDto of(final Credentials credentials, final Long couponId) {
        return new CouponSaveDto(credentials.getId(), credentials.getEmail(), credentials.getPassword(), couponId);
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getCouponId() {
        return couponId;
    }
}
