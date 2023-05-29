package cart.domain;

import java.time.LocalDateTime;

public class MemberCoupon {

    private final Member member;
    private final LocalDateTime issuedDate;
    private final LocalDateTime expiredDate;

    public MemberCoupon(final Member member, final LocalDateTime issuedDate, final LocalDateTime expiredDate) {
        this.member = member;
        this.issuedDate = issuedDate;
        this.expiredDate = expiredDate;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }
}
