package cart.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public class PagedDataResponse<T> {
    private final List<T> data;
    private final PaginationInfoDto pagination;

    public PagedDataResponse(final List<T> data, final PaginationInfoDto pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public static <T> PagedDataResponse<T> from(Page<T> page) {
        final List<T> data = page.getContent();
        final PaginationInfoDto pagination = new PaginationInfoDto(
                (int) page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.getTotalPages()
        );

        return new PagedDataResponse<>(data, pagination);
    }

    public List<T> getData() {
        return data;
    }

    public PaginationInfoDto getPagination() {
        return pagination;
    }
}
