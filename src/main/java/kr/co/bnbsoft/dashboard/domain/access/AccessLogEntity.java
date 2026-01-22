package kr.co.bnbsoft.dashboard.domain.access;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCESS_LOG")
@EntityListeners(AuditingEntityListener.class)
public class AccessLogEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "IP", nullable = false)
    private String ip;

    @Column(name = "URI", nullable = false)
    private String uri;

    @Column(name = "HTTP_STATUS", nullable = false)
    private int httpStatus;

    @CreatedDate
    @Column(name = "ACCESS_AT", nullable = false, updatable = false)
    private String accessAt;
}
