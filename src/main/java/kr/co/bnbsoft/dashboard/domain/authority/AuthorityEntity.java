package kr.co.bnbsoft.dashboard.domain.authority;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.GrantedAuthority;

import dev.retrotv.framework.persistence.jpa.embedded.Use;
import dev.retrotv.framework.persistence.jpa.entity.DateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "AUTHORITIES",
    indexes = {
        @Index(name = "IDX_AUTHORITY_PK", columnList = "AUTHORITY_CODE", unique = true)
    }
)
public class AuthorityEntity extends DateEntity implements GrantedAuthority, Persistable<String> {

    @Id
    @Column(name = "AUTHORITY_CODE")
    private String authorityCode;

    @Column(name = "AUTHORITY_NAME", nullable = false)
    private String authorityName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Embedded
    private Use use;

    /*
     * Persistable 구현을 위한 필드 (해당 row가 신규인지 수정인지 만 판단하는데 사용)
     */
    @Transient
    @Builder.Default
    @Column(name = "IS_NEW")
    private boolean isNew = true;

    @Override
    public String getAuthority() {
        return authorityCode;
    }

    @Override
    public String getId() {
        return this.authorityCode;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void updateMode() {
        this.isNew = false;
    }

    public void insertMode() {
        this.isNew = true;
    }
}
