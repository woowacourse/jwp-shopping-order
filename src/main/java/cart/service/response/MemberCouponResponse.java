package cart.service.response;

public class MemberCouponResponse {

    private final Long id;
    private final String name;

    private MemberCouponResponse() {
        this(null,null);
    }

    public MemberCouponResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
