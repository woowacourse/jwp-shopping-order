package cart.config;

import cart.domain.OrderBaseCouponGenerator;
import cart.domain.OrderPriceBaseCouponGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfiguration {

    @Bean
    public OrderBaseCouponGenerator orderBaseCouponGenerator() {
        return new OrderPriceBaseCouponGenerator();
    }
}
