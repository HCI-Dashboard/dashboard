package kr.co.bnbsoft.dashboard.domain.cluster;

import kr.co.bnbsoft.dashboard.common.dto.DateDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClusterInfoDTO extends DateDTO {
    private String clusterIp;
    private String label;
    private String webhookUrl;

    public ClusterInfoEntity toEntity() {
        return ClusterInfoEntity.builder()
            .clusterIp(this.clusterIp)
            .label(this.label)
            .webhookUrl(this.webhookUrl)
            .build();
    }
}
