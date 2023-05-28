package cart.domain.orderproduct;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;

import java.time.LocalDateTime;

public class Order {

    private Long id;
    private final Member member;
    private final MemberPoint usedPoint;
    private LocalDateTime createdAt;

    public Order(final Member member, final MemberPoint usedPoint) {
        this.member = member;
        this.usedPoint = usedPoint;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Integer getUsedPoint() {
        return usedPoint.getPoint();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}
