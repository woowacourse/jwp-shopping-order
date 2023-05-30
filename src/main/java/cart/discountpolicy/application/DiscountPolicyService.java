package cart.discountpolicy.application;

import cart.cart.Cart;
import cart.cart.domain.cartitem.CartItem;
import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.discountpolicy.discountcondition.DiscountUnit;
import org.springframework.stereotype.Service;

@Service
public class DiscountPolicyService {
    private final DiscountPolicyRepository discountPolicyRepository;

    public DiscountPolicyService(DiscountPolicyRepository discountPolicyRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
    }

    public Long savePolicy(DiscountCondition discountCondition) {
        if (discountCondition.getDiscountTarget() == DiscountTarget.DELIVERY) {
            return saveDiscountPolicyForDelivery(discountCondition);
        }
        if (discountCondition.getDiscountTarget() == DiscountTarget.SPECIFIC) {
            return saveDiscountPolicyForSpecificProduct(discountCondition);
        }
        return saveDiscountPolicyForAllProducts(discountCondition);
    }

    private Long saveDiscountPolicyForSpecificProduct(DiscountCondition discountCondition) {

        if (discountCondition.getDiscountUnit() == DiscountUnit.PERCENTAGE) {
            return discountPolicyRepository.save(new DiscountPolicy(discountCondition) {
                @Override
                public void discount(Cart cart) {
                    for (CartItem cartItem : cart.getCartItems()) {
                        if (discountCondition.getDiscountTargetProductIds().contains(cartItem.getProduct().getId())) {
                            cartItem.setDiscountPrice(calculateDiscountPriceWithPercentage(discountCondition.getDiscountValue(), cartItem.getProduct().getPrice()));
                        }
                    }
                }
            });
        }
        return discountPolicyRepository.save(new DiscountPolicy(discountCondition) {
            @Override
            public void discount(Cart cart) {
                for (CartItem cartItem : cart.getCartItems()) {
                    if (discountCondition.getDiscountTargetProductIds().contains(cartItem.getProduct().getId())) {
                        cartItem.setDiscountPrice(discountCondition.getDiscountValue());
                    }
                }
            }
        });
    }

    private Long saveDiscountPolicyForDelivery(DiscountCondition discountCondition) {
        if (discountCondition.getDiscountUnit() == DiscountUnit.PERCENTAGE) {
            return discountPolicyRepository.save(new DiscountPolicy(discountCondition) {
                @Override
                public void discount(Cart cart) {
                    final var deliveryPrice = cart.getDeliveryPrice();
                    final var discountedDeliveryPrice = calculateDiscountPriceWithPercentage(discountCondition.getDiscountValue(), deliveryPrice);
                    cart.setDeliveryPrice(discountedDeliveryPrice);
                }
            });
        }
        return discountPolicyRepository.save(new DiscountPolicy(discountCondition) {
            @Override
            public void discount(Cart cart) {
                final var deliveryPrice = cart.getDeliveryPrice();
                final var discountedDeliveryPrice = deliveryPrice - discountCondition.getDiscountValue();
                cart.setDeliveryPrice(discountedDeliveryPrice);
            }
        });
    }

    private static int calculateDiscountPriceWithPercentage(int percentage, int price) {
        return Double.valueOf(price * (percentage / 100.0)).intValue();
    }

    private Long saveDiscountPolicyForAllProducts(DiscountCondition discountCondition) {
        if (discountCondition.getDiscountUnit() == DiscountUnit.PERCENTAGE) {
            return discountPolicyRepository.save(new DiscountPolicy(discountCondition) {
                @Override
                public void discount(Cart cart) {
                    for (CartItem cartItem : cart.getCartItems()) {
                        cartItem.setDiscountPrice(calculateDiscountPriceWithPercentage(discountCondition.getDiscountValue(), cartItem.getProduct().getPrice()));
                    }
                }
            });
        }
        return discountPolicyRepository.save(new DiscountPolicy(discountCondition) {
            @Override
            public void discount(Cart cart) {
                for (CartItem cartItem : cart.getCartItems()) {
                    cartItem.setDiscountPrice(discountCondition.getDiscountValue());
                }
            }
        });
    }

    public void applyPolicy(Long discountConditionId, Cart cart) {
        final var discountPolicy = this.discountPolicyRepository.findById(discountConditionId);
        discountPolicy.discount(cart);
    }
}
