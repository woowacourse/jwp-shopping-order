package cart.dto.response;

public class MemberResponse {
    private final Long id;
    private final String email;
    private final long money;
    private final long point;

    public MemberResponse(Long id, String email, long money, long point) {
        this.id = id;
        this.email = email;
        this.money = money;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public long getMoney() {
        return money;
    }

    public long getPoint() {
        return point;
    }
}
