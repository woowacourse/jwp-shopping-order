package cart.application.service.point;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import cart.application.repository.PointRepository;
import cart.application.service.point.dto.PointResultDto;
import cart.domain.Point;
import cart.domain.PointHistory;
import cart.ui.MemberAuth;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class PointReadServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointReadService pointReadService;

    @Test
    @DisplayName("해당 사용자의 포인트 정보를 반환한다")
    void findPointByMemberTest() {
        // given
        List<PointHistory> pointHistoryList = List.of(
                new PointHistory(1L, 0, 1000),
                new PointHistory(2L, 500, 1000),
                new PointHistory(3L, 1000, 3000)
        );
        given(pointRepository.findPointByMemberId(1L))
                .willReturn(new Point(pointHistoryList));

        // when, then
        assertSoftly(softly -> {
            PointResultDto pointByMember = pointReadService.findPointByMember(new MemberAuth(1L, "test", "test@email.com", "test123"));
            softly.assertThat(pointByMember.getTotalPoint()).isEqualTo(3500);
        });
    }

}
