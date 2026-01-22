package kr.co.bnbsoft.dashboard.domain.cluster;

import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import dev.retrotv.framework.persistence.jpa.entity.DateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "CLUSTER_INFO",
    indexes = {
        @Index(name = "IDX_CLUSTER_INFO_PK", columnList = "CLUSTER_IP", unique = true)
    }
)
public class ClusterInfoEntity extends DateEntity {

    @Id
    @Column(name = "CLUSTER_IP")
    private String clusterIp;

    @Column(name = "LABEL", nullable = false, unique = true)
    private String label;

    @Column(name = "WEBHOOK_URL", nullable = true)
    private String webhookUrl;

    public ClusterInfoDTO toDTO() {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String createdAt = this.getCreatedAt() != null ? this.getCreatedAt().format(fmt) : null;
        String modifiedAt = this.getModifiedAt() != null ? this.getModifiedAt().format(fmt) : null;

        return ClusterInfoDTO.builder()
            .clusterIp(this.clusterIp)
            .label(this.label)
            .webhookUrl(this.webhookUrl)
            .createdAt(createdAt)
            .createdId(this.getCreatedId())
            .modifiedAt(modifiedAt)
            .modifiedId(this.getModifiedId())
            .build();
    }
}
