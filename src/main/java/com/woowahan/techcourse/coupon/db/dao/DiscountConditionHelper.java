package com.woowahan.techcourse.coupon.db.dao;

import com.woowahan.techcourse.coupon.domain.discountcondition.AlwaysDiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountcondition.DiscountCondition;
import com.woowahan.techcourse.coupon.exception.CouponException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.springframework.jdbc.core.RowMapper;

public enum DiscountConditionHelper {
    ALWAYS("always", (resultSet, rowNum) -> new AlwaysDiscountCondition());

    private final String name;
    private final RowMapper<? extends DiscountCondition> rowMapper;

    DiscountConditionHelper(String name, RowMapper<? extends DiscountCondition> rowMapper) {
        this.name = name;
        this.rowMapper = rowMapper;
    }

    public static DiscountConditionHelper findByName(String name) {
        return Arrays.stream(values())
                .filter(discountPolicyHelper -> discountPolicyHelper.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new CouponException("해당하는 할인 조건이 없습니다."));
    }

    public DiscountCondition mapRow(ResultSet rs, int i) throws SQLException {
        return rowMapper.mapRow(rs, i);
    }
}
