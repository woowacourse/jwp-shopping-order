package cart.entity;

public class MemberEntity {

    private final Long id;
    private final String email;
    private final String password;
    private final String rank;
    private final long totalPurchaseAmount;

    public MemberEntity(
            final Long id,
            final String email,
            final String password,
            final String rank,
            final long totalPurchaseAmount
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rank = rank;
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRank() {
        return rank;
    }

    public long getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }
}
