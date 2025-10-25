package dev.retrotv.dashboard.domain.authority;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO implements Serializable {
    private String authorityCode;
    private String authorityName;
    private String description;
    private String createdDate;
    private String createdId;
    private String modifiedDate;
    private String modifiedId;

    public AuthorityEntity toEntity() {
        return AuthorityMapper.INSTANCE.toEntity(this);
    }
}
