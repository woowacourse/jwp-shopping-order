package cart.entity;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;
    private final Long money;
    private final Long point;

    public MemberEntity(Long id, String email, String password, Long money, Long point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = money;
        this.point = point;
    }

    public MemberEntity(String email, String password, Long money, Long point) {
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

    public Long getMoney() {
        return money;
    }

    public Long getPoint() {
        return point;
    }
}
