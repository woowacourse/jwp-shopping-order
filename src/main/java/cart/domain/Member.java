package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private String member_rank;
    private int totalPrice;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String password, String member_rank, int totalPrice) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.member_rank = member_rank;
        this.totalPrice = totalPrice;
    }

    public Member(String email, String password, String member_rank) {
        this.email = email;
        this.password = password;
        this.member_rank = member_rank;
    }

    public void createOrder(int totalPrice){
        this.totalPrice += totalPrice;
        setRank();
    }

    public void setRank() {
        if (totalPrice == 0) {
            this.member_rank = "일반";
        }else if(totalPrice <= 100_000){
            this.member_rank = "silver";
        }else if(totalPrice <= 200_000){
            this.member_rank = "gold";
        }else if(totalPrice <= 300_000){
            this.member_rank = "platinum";
        }else{
            this.member_rank = "diamond";
        }
    }

    public int findDiscountedPercentage() {
        if (member_rank.equals("일반")) {
            return 0;
        }else if(member_rank.equals("silver")){
            return 5;
        }else if(member_rank.equals("gold")){
            return 10;
        }else if(member_rank.equals("platinum")){
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

    public String getMemberRank() {
        return member_rank;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
