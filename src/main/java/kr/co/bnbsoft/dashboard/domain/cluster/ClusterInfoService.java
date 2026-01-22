package kr.co.bnbsoft.dashboard.domain.cluster;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClusterInfoService {
    private final ClusterInfoRepository clusterInfoRepository;

    public List<ClusterInfoDTO> getClusterInfoList() {
        List<ClusterInfoEntity> clusters = clusterInfoRepository.findAll();
        return clusters.stream()
            .map(ClusterInfoEntity::toDTO)
            .toList();
    }
}
