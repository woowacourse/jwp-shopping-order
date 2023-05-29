package cart;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cart.domain.PaginationPolicy;
import cart.domain.Paginator;

@Configuration
public class BeanConfig {

    @Bean
    public Paginator paginator() {
        return new Paginator(new PaginationPolicy());
    }
}
