package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private String rank;
    private int totalPrice;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String password, String rank, int totalPrice) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rank = rank;
        this.totalPrice = totalPrice;
    }

    public Member(String email, String password, String rank) {
        this.email = email;
        this.password = password;
        this.rank = rank;
    }

    public void order(int totalPrice){
        this.totalPrice += totalPrice;
        setRank();
    }

    //TODO: 상품 주문할 때 마다 rank를 calculate해줘야한다
    public void setRank() {
        if (totalPrice == 0) {
            this.rank = "일반";
        }else if(totalPrice <= 100_000){
            this.rank = "silver";
        }else if(totalPrice <= 200_000){
            this.rank = "gold";
        }else if(totalPrice <= 300_000){
            this.rank = "platinum";
        }else{
            this.rank = "diamond";
        }
    }

    public int calculateDiscountedPercentage() {
        if (rank.equals("일반")) {
            return 0;
        }else if(rank.equals("silver")){
            return 5;
        }else if(rank.equals("gold")){
            return 10;
        }else if(rank.equals("platinum")){
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

    public String getRank() {
        return rank;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
