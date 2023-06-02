package cart.dto;

import cart.domain.MemberGrade;

public final class DiscountInformationRequest {
    private int price;
    private MemberGrade memberGrade;

    public DiscountInformationRequest() {
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
