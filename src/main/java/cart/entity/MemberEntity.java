package cart.entity;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer money;
    private final Integer point;

    public MemberEntity(Long id, String email, String password, Integer money, Integer point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = money;
        this.point = point;
    }

    public MemberEntity(String email, String password, Integer money, Integer point) {
        this(0L, email, password, money, point);
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

    public Integer getMoney() {
        return money;
    }

    public Integer getPoint() {
        return point;
    }
}
