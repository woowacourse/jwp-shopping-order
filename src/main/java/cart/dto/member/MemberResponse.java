package cart.dto.member;

import cart.domain.member.Member;

public class MemberResponse {

    private Long id;
    private String rank;
    private int discountRate;

    public MemberResponse(final Long id, final String rank, final int discountRate) {
        this.id = id;
        this.rank = rank;
        this.discountRate = discountRate;
    }

    public MemberResponse(final Member member) {
        this(member.getId(), member.getRank().getKorean(), member.getRank().getDiscountRate());
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
