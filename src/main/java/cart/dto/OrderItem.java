package cart.dto;

import java.beans.ConstructorProperties;

public class OrderItem {
    private final Long id;

    @ConstructorProperties("id")
    public OrderItem(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
