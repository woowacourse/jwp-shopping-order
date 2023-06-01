package cart.application.config;

import cart.domain.discountpolicy.StorePaymentAmountPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingConfig {

    @Bean
    public StorePaymentAmountPolicy paymentDiscountPolicy() {
        return new StorePaymentAmountPolicy();
    }
}
