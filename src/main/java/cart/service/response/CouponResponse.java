package cart.service.response;

public class CouponResponse {

    private final Long id;
    private final String name;

    private CouponResponse() {
        this(null, null);
    }

    public CouponResponse(final Long id, final String name) {
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
