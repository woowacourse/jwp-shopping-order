package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class PaginationTest {

    @DisplayName("전체 아이템 개수와 현재 페이지를 입력하면 Pagination을 반환한다")
    @Test
    void create() {
        // given
        int totalItems = 53;
        int currentPage = 2;

        // when
        Pagination pagination = Pagination.create(totalItems, currentPage);

        // then
        assertAll(
                () -> assertThat(pagination.getTotalPage()).isEqualTo(6),
                () -> assertThat(pagination.getCurrentPage()).isEqualTo(2),
                () -> assertThat(pagination.getFirstItemIndex()).isEqualTo(10),
                () -> assertThat(pagination.getPageSize()).isEqualTo(10)
        );
    }
}
