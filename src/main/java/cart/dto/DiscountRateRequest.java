package cart.dto;

import cart.domain.MemberGrade;

public final class DiscountRateRequest {
    private int price;
    private MemberGrade memberGrade;

    public DiscountRateRequest() {
    }

    public int getPrice() {
        return price;
    }

    public MemberGrade getMemberGrade() {
        return memberGrade;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public void setMemberGrade(final MemberGrade memberGrade) {
        this.memberGrade = memberGrade;
    }
}
