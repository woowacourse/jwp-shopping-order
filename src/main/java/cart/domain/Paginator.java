package cart.domain;

import java.util.Collections;
import java.util.List;

public class Paginator {

    private final PaginationPolicy paginationPolicy;

    public Paginator(PaginationPolicy paginationPolicy) {
        this.paginationPolicy = paginationPolicy;
    }

    public List<Order> paginate(List<Order> orders, int page) {
        return Collections.emptyList();
    }
}
