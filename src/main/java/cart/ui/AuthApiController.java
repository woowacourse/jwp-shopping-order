package cart.ui;

import cart.application.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthApiController {
    private final LoginService loginService;

    public AuthApiController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestHeader("Authorization") final String authorization) {
        loginService.login(authorization);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
