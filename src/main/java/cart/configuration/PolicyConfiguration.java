package cart.configuration;

import cart.domain.PointDiscountPolicy;
import cart.domain.PointEarnPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyConfiguration {

    @Bean
    public PointDiscountPolicy pointDiscountPolicy() {
        return PointDiscountPolicy.DEFAULT;
    }

    @Bean
    public PointEarnPolicy pointEarnPolicy() {
        return PointEarnPolicy.DEFAULT;
    }
}
