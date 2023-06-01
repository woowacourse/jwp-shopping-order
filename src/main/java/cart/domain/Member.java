package cart.domain;

import cart.exception.MemberException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Member {

    public static final int MINIMUM_PASSWORD_LENGTH = 1;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");

    private final Long id;
    private final String email;
    private final String password;
    private Integer money;
    private Integer point;

    public Member(Long id, String email, String password, Integer money, Integer point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = money;
        this.point = point;
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, 0, 0);
        validate(id, email);
    }

    private void validate(Long id, String email) {
        validateId(id);
        validateEmail(email);
    }

    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new MemberException.InvalidIdByNull();
        }
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new MemberException.InvalidEmail();
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

    public Integer getMoney() {
        return money;
    }

    public Integer getPoint() {
        return point;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addPoint(Integer point) {
        if (point < 0) {
            throw new IllegalArgumentException("적립하는 포인트는 양수여야합니다.");
        }
        this.point += point;
    }

    public void usePoint(Integer usePoint) {
        if (point < usePoint) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        this.point -= usePoint;
    }

    public void useMoney(Integer useMoney) {
        if (money < useMoney) {
            throw new IllegalArgumentException("금액이 부족합니다.");
        }
        this.money -= useMoney;
    }
}
