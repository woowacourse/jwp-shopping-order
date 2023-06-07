package cart.service;

import cart.dto.PageInfo;
import cart.dto.PageRequest;
import cart.paging.Paging;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PagingTest {

    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        pageRequest = new PageRequest(2, 10);
    }

    @Test
    void 데이터_시작점을_얻는다() {
        //given
        final Paging paging = new Paging(pageRequest);

        //when
        final int start = paging.getStart();

        //then
        assertThat(start).isEqualTo(10);
    }

    @Test
    void 데이터_사이즈를_얻는다() {
        //given
        final Paging paging = new Paging(pageRequest);

        //when
        final int size = paging.getSize();

        //then
        assertThat(size).isEqualTo(10);
    }

    @Test
    void 페이지_정보를_얻는다() {
        //given
        final Paging paging = new Paging(pageRequest);

        //when
        final PageInfo pageInfo = paging.getPageInfo(21);

        //then
        assertThat(pageInfo).usingRecursiveComparison()
                .isEqualTo(new PageInfo(2, 10, 21, 3));
    }
}
