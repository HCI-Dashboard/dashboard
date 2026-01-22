package kr.co.bnbsoft.dashboard.domain.cluster;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.retrotv.framework.foundation.common.response.Response;
import dev.retrotv.framework.foundation.common.response.MultipleDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clusters")
public class ClusterInfoController {
    private final ClusterInfoService clusterInfoService;

    @GetMapping()
    public ResponseEntity<Response> getClusterInfo() {
        return ResponseEntity.ok(
            new MultipleDataResponse<>(
                "클러스터 정보 조회 성공",
                clusterInfoService.getClusterInfoList()
            )
        );
    }
}
