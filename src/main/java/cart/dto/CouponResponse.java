package cart.dto;

public class CouponResponse {
    private final Long id;
    private final String name;

    public CouponResponse(Long id, String name) {
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
