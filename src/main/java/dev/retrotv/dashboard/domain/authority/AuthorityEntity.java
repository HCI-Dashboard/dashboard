package dev.retrotv.dashboard.domain.authority;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.GrantedAuthority;

import dev.retrotv.framework.persistence.jpa.entity.DateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Comment("권한 테이블")
@Table(
    name = "AUTHORITIES",
    indexes = {
        @Index(name = "IDX_AUTHORITY_CODE_PK", columnList = "AUTHORITY_CODE", unique = true)
    }
)
public class AuthorityEntity extends DateEntity implements GrantedAuthority, Persistable<String> {
    
    @Id
    @Size(max = 20)
    @Comment("권한 코드")
    @Column(name = "AUTHORITY_CODE", nullable = false, unique = true)
    private String authorityCode;

    @Size(max = 20)
    @Comment("권한 명")
    @Column(name = "AUTHORITY_NAME", nullable = false)
    private String authorityName;

    @Size(max = 200)
    @Comment("권한 설명")
    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    @Builder.Default
    @Comment("신규 여부")
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

    public AuthorityDTO toDTO() {
        return AuthorityMapper.INSTANCE.toDTO(this);
    }
}
