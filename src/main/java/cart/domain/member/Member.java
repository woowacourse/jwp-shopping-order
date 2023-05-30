package cart.domain.member;

import java.util.Collections;
import java.util.List;

public class Member {

    private final MemberName name;
    private final MemberPassword password;
    private final List<MemberCoupon> memberCoupons;

    private Member(final MemberName name) {
        this(name, null, null);
    }

    private Member(final MemberName name, final MemberPassword password, final List<MemberCoupon> memberCoupons) {
        this.name = name;
        this.password = password;
        this.memberCoupons = memberCoupons;
    }

    public static Member create(final String name) {
        return new Member(MemberName.create(name));
    }

    public static Member create(final String name, final String password) {
        return new Member(MemberName.create(name), MemberPassword.create(password), Collections.emptyList());
    }

    public static Member createWithEncodedPassword(final String name, final String password) {
        return new Member(MemberName.create(name), MemberPassword.createWithEncodedPassword(password),
            Collections.emptyList());
    }

    public String name() {
        return name.getName();
    }

    public String password() {
        return password.getPassword();
    }
}
