package cart.config.auth;

import cart.application.MemberService;
import cart.infrastructure.auth.BasicAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthProviderConfig {

    @Bean
    public AuthProvider basicAuthProvider(MemberService memberService) {
        return new BasicAuthProvider(memberService);
    }
}
