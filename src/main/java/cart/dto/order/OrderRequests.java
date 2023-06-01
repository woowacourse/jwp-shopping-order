package cart.dto.order;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequests {

    @Valid
    @NotNull
    private List<OrderRequest> orderRequests;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public OrderRequests(final List<OrderRequest> orderRequests) {
        this.orderRequests = orderRequests;
    }

    public List<OrderRequest> getOrderRequests() {
        return orderRequests;
    }
}
