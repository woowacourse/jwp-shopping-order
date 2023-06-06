package cart.ui;

import cart.application.UserService;
import cart.domain.Member;
import cart.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUser(final Member member) {
        final UserResponse userResponse = userService.getUserResponse(member);
        return ResponseEntity.ok().body(userResponse);
    }
}
