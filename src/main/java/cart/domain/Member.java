package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private String grade;
    private int totalPurchaseAmount;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String password, String grade, int totalPurchaseAmount) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.grade = grade;
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public Member(String email, String password, String grade) {
        this.email = email;
        this.password = password;
        this.grade = grade;
    }

    public void createOrder(int totalPrice){
        this.totalPurchaseAmount += totalPrice;
        this.grade = Grade.findGrade(totalPurchaseAmount);
    }

    public int findDiscountedPercentage() {
        return Grade.findGradeDiscount(grade);
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getGrade() {
        return grade;
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }
}
