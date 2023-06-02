package cart.dto.response;

public class MemberResponse {
    private final Long id;
    private final String email;
    private final Integer money;
    private final Integer point;

    public MemberResponse(Long id, String email, Integer money, Integer point) {
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

    public Integer getMoney() {
        return money;
    }

    public Integer getPoint() {
        return point;
    }
}
