package cart.domain;

import cart.exception.PointNotEnoughException;
import java.util.Objects;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private Point point;

    public Member(Long id, String email, String password, long point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = new Point(point);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void increasePoint(Point point) {
        this.point = this.point.plus(point);
    }

    public void decreasePoint(Point point) {
        if (hasNotEnoughPointToUse(point)) {
            throw new PointNotEnoughException("포인트가 부족합니다.");
        }
        this.point = this.point.minus(point);
    }

    private boolean hasNotEnoughPointToUse(Point point) {
        return this.point.getAmount() < point.getAmount();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
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

    public Point getPoint() {
        return point;
    }


    public static final class Builder {
        private Long id;
        private String email;
        private String password;
        private long point;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder point(long point) {
            this.point = point;
            return this;
        }

        public Member build() {
            return new Member(id, email, password, point);
        }
    }
}
