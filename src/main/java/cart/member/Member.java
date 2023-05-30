package cart.member;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private Long id;
    private String email;
    private String password;
    private List<Long> coupons = new ArrayList<>();

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String password, List<Long> coupons) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void giveCoupon(long couponId) {
        this.coupons.add(couponId);
    }

    public List<Long> getCouponIds() {
        return coupons;
    }
}
