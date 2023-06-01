package cart.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import cart.domain.PointAddition;

@Repository
public class PointAdditionDao {

    public List<PointAddition> findAllByMemberId(Long memberId) {
        return Collections.emptyList();
    }

    public Long insert(PointAddition pointAddition) {
        return null;
    }
}
