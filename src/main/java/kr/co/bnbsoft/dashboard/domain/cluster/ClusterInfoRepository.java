package kr.co.bnbsoft.dashboard.domain.cluster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterInfoRepository extends JpaRepository<ClusterInfoEntity, String> {

}
