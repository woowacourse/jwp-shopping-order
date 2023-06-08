package cart.dto;

import cart.domain.CartItem;
import cart.domain.Member;

public class MemberResponse {

    private Long id;
    private String rank;
    private int discountRate;

    public MemberResponse() {
    }

    private MemberResponse(Long id, String rank, int discountRate) {
        this.id = id;
        this.rank = rank;
        this.discountRate = discountRate;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getGrade(),
                member.findDiscountedPercentage()
        );
    }


    public Long getId() {
        return id;
    }

    public String getRank() {
        return rank;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
