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
        setGrade();
    }

    public void setGrade() {
        if (totalPurchaseAmount == 0) {
            this.grade = "일반";
        }else if(totalPurchaseAmount <= 100_000){
            this.grade = "silver";
        }else if(totalPurchaseAmount <= 200_000){
            this.grade = "gold";
        }else if(totalPurchaseAmount <= 300_000){
            this.grade = "platinum";
        }else{
            this.grade = "diamond";
        }
    }

    public int findDiscountedPercentage() {
        if (grade.equals("일반")) {
            return 0;
        }else if(grade.equals("silver")){
            return 5;
        }else if(grade.equals("gold")){
            return 10;
        }else if(grade.equals("platinum")){
            return 15;
        }else{
            return 20;
        }
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
