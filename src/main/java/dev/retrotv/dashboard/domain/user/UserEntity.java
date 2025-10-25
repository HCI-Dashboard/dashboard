package dev.retrotv.dashboard.domain.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import dev.retrotv.dashboard.domain.authority.AuthorityEntity;
import dev.retrotv.framework.persistence.jpa.converter.BooleanYNConverter;
import dev.retrotv.framework.persistence.jpa.entity.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Comment("사용자 테이블")
@Table(
    name = "USERS",
    indexes = {
        @Index(name = "IDX_USERS_PK", columnList = "USERNAME", unique = true),
    }
)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)

// 삭제 쿼리 실행 시, NON_EXPIRED, ENABLED 컬럼을 'N'으로 업데이트
@SQLDelete(sql = "UPDATE USERS SET NON_EXPIRED = 'N', ENABLED = 'N' WHERE USERNAME = ? AND NON_EXPIRED = 'Y'")
public class UserEntity extends DateEntity implements UserDetails {

    @Id
    @Size(max = 50)
    @Comment("사용자 명")
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Size(max = 100)
    @Comment("비밀번호")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Size(max = 50)
    @Comment("이메일")
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Comment("사용자 권한")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USER_AUTHORITY",
        joinColumns = @JoinColumn(name = "USERNAME"),
        inverseJoinColumns = @JoinColumn(name = "AUTHORITY_CODE")
    )
    private Set<AuthorityEntity> authorities;

    /*
     * 사용자의 계정 상태를 나타내는 속성 정보
     * -----------------------------------------------------------------------------
     * Expired: 어떠한 이유로든 완전 사용 불가 상태 (Ex. 탈퇴 처리된 계정)
     * Locked: 계정이 잠겨 사용할 수 없는 상태 (Ex. 패스워드 5회 이상 틀린 계정)
     * Credentials Expired: 패스워드 사용 만료 여부 (Ex. 일정기간 이상 패스워드 변경 X, 임시 패스워드 사용중인 계정)
     * Enabled: 계정 활성 여부 (Ex. E-Mail, SMS 등으로 인증한 경우에 Enabled 상태가 됨)
     */
    @Size(max = 1)
    @Builder.Default
    @Comment("계정 만료 여부")
    @Column(name = "NON_EXPIRED", nullable = false)
    @Convert(converter = BooleanYNConverter.class)
    private boolean nonExpired = true;

    @Size(max = 1)
    @Builder.Default
    @Comment("계정 잠금 여부")
    @Column(name = "NON_LOCKED", nullable = false)
    @Convert(converter = BooleanYNConverter.class)
    private boolean nonLocked = true;

    @Size(max = 1)
    @Builder.Default
    @Comment("자격증명 만료 여부")
    @Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false)
    @Convert(converter = BooleanYNConverter.class)
    private boolean credentialsNonExpired = true;

    @Size(max = 1)
    @Builder.Default
    @Comment("계정 활성 여부")
    @Column(name = "ENABLED", nullable = false)
    @Convert(converter = BooleanYNConverter.class)
    private boolean enabled = false;

    /*
     * 이 계정이 가진 권한들을 반환합니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /*
     * 이 계정의 패스워드를 반환합니다.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /*
     * 이 계정의 계정명을 반환힙니다.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /*
     * 사용자의 계정이 만료되었는지 확인합니다.
     */
    @Override
    public boolean isAccountNonExpired() {
        return nonExpired;
    }

    /*
     * 사용자의 계정이 잠겼는지 확인합니다.
     */
    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    /*
     * 사용자의 자격증명(password)이 만료되었는지 확인합니다.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /*
     * 사용자의 활성화 여부를 반환합니다.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 계정을 파기 처리 합니다.
     */
    public void expire() {
        nonExpired = false;
    }

    /**
     * 계정을 파기 취소 합니다.
     */
    public void unexpire() {
        nonExpired = true;
    }

    /**
     * 계정을 잠금 처리 합니다.
     */
    public void lock() {
        nonLocked = false;
    }

    /**
     * 계정을 잠금을 해제 합니다.
     */
    public void unlock() {
        nonLocked = true;
    }

    /**
     * 계정 인증 수단을 만료 처리 합니다.
     */
    public void credentialsExpire() {
        credentialsNonExpired = false;
    }

    /**
     * 계정 인증 수단을 만료 취소 합니다.
     */
    public void credentialsUnexpire() {
        credentialsNonExpired = true;
    }

    /**
     * 계정을 활성화(Enabled) 처리 합니다.
     */
    public void enable() {
        enabled = true;
    }

    /**
     * 계정을 비활성화(Disabled) 처리 합니다.
     */
    public void disable() {
        enabled = false;
    }

    /**
     * 패스워드를 업데이트 합니다.
     *
     * @param newPassword 인코딩 된, 새 패스워드
     */
    public void updatePassword(String newPassword) {
        password = newPassword;
    }

    /**
     * 권한을 업데이트 합니다.
     * 
     * @param authorities 새 권한들
     */
    public void updateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<AuthorityEntity> newAuthorities = new HashSet<>();
        authorities.forEach(authority -> newAuthorities.add((AuthorityEntity) authority));

        // 기존 authorities와 동일할 경우 업데이트 하지 않는다
        if (!authorities.containsAll(newAuthorities)) {
            this.authorities = newAuthorities;
        }
    }

    public void removeAuthorities() {
        authorities = null;
    }

    public void removeAuthorities(String authorityCode) {
        authorities.removeIf(authority -> authority.getAuthority().equals(authorityCode));
    }

    public UserDTO toDTO() {
        return UserMapper.INSTANCE.toDTO(this, this.authorities);
    }

    public UserDTO toExcloudPasswordDTO() {
        return UserMapper.INSTANCE.toExcloudPasswordDTO(this, this.authorities);
    }
}
