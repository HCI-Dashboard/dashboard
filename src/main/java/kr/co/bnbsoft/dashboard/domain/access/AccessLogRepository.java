package kr.co.bnbsoft.dashboard.domain.access;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLogEntity, Long> {

}
