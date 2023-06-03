package cart.ui.dto;

public class MemberGradeResponse {

    private Long id;
    private String rank;
    private int discountRate;

    public MemberGradeResponse(final Long id, final String rank, final int discountRate) {
        this.id = id;
        this.rank = rank;
        this.discountRate = discountRate;
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
