package cart.config.point;

import cart.domain.price.FixedPercentPointPolicy;
import cart.domain.price.PointPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PointPolicyConfig {
    private static final double PERCENT = 10;

    @Bean
    public PointPolicy pointPolicy() {
        return new FixedPercentPointPolicy(PERCENT);
    }
}
