package cart.domain;

public class Point {

    private final Long id;
    private final Integer point;
    private final Member member;

    public Point(Long id, Integer point, Member member) {
        this.id = id;
        this.point = point;
        this.member = member;
    }

    public Point(Long id, Member member) {
        this(id, null, member);
    }

    public boolean isLessThan(int value) {
        return this.point < value;
    }

    public boolean isGreaterThan(int value) {
        return this.point > value;
    }

    public Point changeBy(int difference) {
        return new Point(id, point + difference, member);
    }

    public Long getId() {
        return id;
    }

    public Integer getPoint() {
        return point;
    }

    public Member getMember() {
        return member;
    }
}
