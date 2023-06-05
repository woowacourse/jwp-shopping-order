package cart.ui;

import cart.domain.Member;
import cart.ui.auth.Login;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthApiController {

    @SecurityRequirement(name = "basicAuth")
    @PostMapping("login")
    public ResponseEntity<Void> login(@Login Member member) {
        return ResponseEntity.ok().build();
    }
}
