package com.woowahan.techcourse.coupon.db.dao;

import com.woowahan.techcourse.coupon.domain.discountpolicy.DiscountPolicy;
import com.woowahan.techcourse.coupon.domain.discountpolicy.PercentDiscountPolicy;
import com.woowahan.techcourse.coupon.exception.CouponException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.springframework.jdbc.core.RowMapper;

public enum DiscountPolicyHelper {
    PERCENT("percent", (resultSet, rowNum) -> new PercentDiscountPolicy(resultSet.getInt("ad_rate")));

    private final String name;
    private final RowMapper<? extends DiscountPolicy> rowMapper;

    DiscountPolicyHelper(String name, RowMapper<? extends DiscountPolicy> rowMapper) {
        this.name = name;
        this.rowMapper = rowMapper;
    }

    public static DiscountPolicyHelper findByName(String name) {
        return Arrays.stream(values())
                .filter(discountPolicyHelper -> discountPolicyHelper.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new CouponException("해당하는 할인 정책이 없습니다."));
    }

    public DiscountPolicy mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rowMapper.mapRow(rs, rowNum);
    }
}
