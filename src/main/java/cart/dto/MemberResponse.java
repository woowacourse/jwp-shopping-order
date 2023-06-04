package cart.dto;

public class MemberResponse {

    private final long id;
    private final String email;
    private final int money;
    private final int point;

    public MemberResponse(long id, String email, int money, int point) {
        this.id = id;
        this.email = email;
        this.money = money;
        this.point = point;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getMoney() {
        return money;
    }

    public int getPoint() {
        return point;
    }
}
