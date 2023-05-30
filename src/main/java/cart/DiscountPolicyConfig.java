package cart;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cart.domain.price.DefaultPricePolicy;
import cart.domain.price.PricePolicy;
import cart.domain.price.discount.grade.BasicGradeDiscountPolicy;
import cart.domain.price.discount.price.BasicPriceDiscountPolicy;

@Configuration
public class DiscountPolicyConfig {
    @Bean
    public PricePolicy configureDiscountPolicy() {
        return new DefaultPricePolicy(List.of(
                new BasicGradeDiscountPolicy(),
                new BasicPriceDiscountPolicy()
        ));
    }
}
