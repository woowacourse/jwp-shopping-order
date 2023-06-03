package cart.entity;

public class MemberEntity {

    private final Long id;
    private final String email;
    private final String password;
    private final String grade;
    private final int totalPurchaseAmount;

    public MemberEntity(
            final Long id,
            final String email,
            final String password,
            final String grade,
            final int totalPurchaseAmount
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.grade = grade;
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

    public String getGrade() {
        return grade;
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }
}
