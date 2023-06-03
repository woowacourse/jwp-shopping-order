package cart.application.config;

import cart.domain.discountpolicy.StoreDiscountPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfig {

    @Bean
    public StoreDiscountPolicy discountPolicy() {
        return new StoreDiscountPolicy();
    }
}
