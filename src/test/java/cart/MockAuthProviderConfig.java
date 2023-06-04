package cart;

import cart.config.auth.AuthProvider;
import cart.config.auth.BasicAuthProvider;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockAuthProviderConfig {
    @Bean
    public AuthProvider basicAuthProvider() {
        return Mockito.mock(BasicAuthProvider.class);
    }
}
