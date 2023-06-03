package cart.domain.member;

import java.util.Collections;
import java.util.List;

public class Member {

    private final MemberName name;
    private final MemberPassword password;
    private final List<MemberCoupon> memberCoupons;

    private Member(final MemberName name, final MemberPassword password, final List<MemberCoupon> memberCoupons) {
        this.name = name;
        this.password = password;
        this.memberCoupons = memberCoupons;
    }

    public static Member create(final String name, final MemberPassword password) {
        return new Member(MemberName.create(name), password, Collections.emptyList());
    }

    public static Member create(final String name, final MemberPassword password,
                                final List<MemberCoupon> memberCoupons) {
        return new Member(MemberName.create(name), password, memberCoupons);
    }

    public boolean isSameName(final String memberName) {
        return name.equals(MemberName.create(memberName));
    }

    public String name() {
        return name.getName();
    }

    public String password() {
        return password.getPassword();
    }

    public MemberPassword memberPassword() {
        return password;
    }

    public List<MemberCoupon> memberCoupons() {
        return memberCoupons;
    }
}
