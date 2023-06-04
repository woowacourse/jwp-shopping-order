package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import cart.domain.Point;
import cart.dto.UserResponse;
import cart.fixture.MemberFixture;
import cart.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PointRepository pointRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(pointRepository);
    }

    @Test
    void getUserResponse() {
        given(pointRepository.getTotalLeftPoint(any())).willReturn(Point.valueOf(1000));
        final UserResponse result = userService.getUserResponse(MemberFixture.MEMBER);
        assertAll(
                () -> assertThat(result.getEmail()).isEqualTo("odo1@woowa.com"),
                () -> assertThat(result.getPoint()).isEqualTo(1000),
                () -> assertThat(result.getEarnRate()).isEqualTo(5)
        );
    }
}
