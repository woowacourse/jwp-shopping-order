package cart;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cart.domain.discount.DefaultDiscountPolicy;
import cart.domain.discount.DiscountPolicy;
import cart.domain.discount.grade.GradeDiscountPolicy;
import cart.domain.discount.price.PriceDiscountPolicy;

@Configuration
public class DiscountPolicyConfiguration {

    @Bean
    public DiscountPolicy configDiscountPolicy() {
        return new DefaultDiscountPolicy(
                List.of(
                        new PriceDiscountPolicy(),
                        new GradeDiscountPolicy()
                )
        );
    }
}
