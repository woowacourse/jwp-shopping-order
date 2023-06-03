package cart.dto.request;

import java.beans.ConstructorProperties;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class OrderItemRequest {

    @NotNull
    private Long id;
    @NotNull
    @Range(min = 1)
    private Integer quantity;

    @ConstructorProperties(value = {"id", "quantity"})
    public OrderItemRequest(final Long id, final Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
