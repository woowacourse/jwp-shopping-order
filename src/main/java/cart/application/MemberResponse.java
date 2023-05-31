package cart.application;

public class MemberResponse {

    private Long id;
    private String rank;
    private int discountRate;

    public MemberResponse() {
    }

    public MemberResponse(Long id, String rank, int discountRate) {
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
