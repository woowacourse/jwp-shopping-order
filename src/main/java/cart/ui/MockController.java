package cart.ui;

import cart.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockController {

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok().body(new UserResponse("odo27@naver.com", 1000L, 5));
    }
}
