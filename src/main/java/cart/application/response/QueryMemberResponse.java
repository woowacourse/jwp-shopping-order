package cart.application.response;

import cart.domain.member.Member;

public class QueryMemberResponse {

    private final Long id;
    private final String email;
    private final int money;
    private final int point;

    public QueryMemberResponse(Long id, String email, int money, int point) {
        this.id = id;
        this.email = email;
        this.money = money;
        this.point = point;
    }

    public static QueryMemberResponse from(Member member) {
        return new QueryMemberResponse(
                member.getId(),
                member.getEmail(),
                member.getMoney().getValue().intValue(),
                member.getPoint().getValue().intValue()
        );
    }

    public Long getId() {
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
