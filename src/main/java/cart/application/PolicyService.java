package cart.application;

import cart.dao.DefaultDeliveryPolicyDao;
import cart.dao.DefaultDiscountPolicyDao;
import cart.dto.DefaultDeliveryPolicyResponse;
import cart.dto.DefaultDiscountPolicyResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolicyService {

    private final DefaultDeliveryPolicyDao defaultDeliveryPolicyDao;
    private final DefaultDiscountPolicyDao defaultDiscountPolicyDao;

    public PolicyService(final DefaultDeliveryPolicyDao defaultDeliveryPolicyDao, final DefaultDiscountPolicyDao defaultDiscountPolicyDao) {
        this.defaultDeliveryPolicyDao = defaultDeliveryPolicyDao;
        this.defaultDiscountPolicyDao = defaultDiscountPolicyDao;
    }

    public List<DefaultDiscountPolicyResponse> getDefaultDiscountPolicy() {
        return this.defaultDiscountPolicyDao.findDefault().stream()
                .map(DefaultDiscountPolicyResponse::from)
                .collect(Collectors.toList());
    }

    public List<DefaultDeliveryPolicyResponse> getDefaultDeliveryPolicy() {
        return this.defaultDeliveryPolicyDao.findDefault().stream()
                .map(DefaultDeliveryPolicyResponse::from)
                .collect(Collectors.toList());
    }
}
