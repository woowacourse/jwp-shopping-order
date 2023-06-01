package cart.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import cart.domain.PointUsage;

@Repository
public class PointUsageDao {

    public List<PointUsage> findAllByMemberId(Long memberId) {
        return Collections.emptyList();
    }

    public Long insert(PointUsage pointUsage) {
        return null;
    }

    public void insertAll(List<PointUsage> pointUsages) {
    }
}
