package dev.retrotv.dashboard.domain.user;

import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

import dev.retrotv.dashboard.domain.authority.AuthorityDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private String username;
    private String password;
    private String email;
    private String newPassword;
    private Set<AuthorityDTO> authorities;
    private Boolean nonExpired;
    private Boolean nonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private String createdDate;
    private String createdId;
    private String modifiedDate;
    private String modifiedId;

    public UserEntity toEntity() {
        return UserMapper.INSTANCE.toEntity(this);
    }

    @Override
    public String toString() {
        return "{" +
               "    username: " + username + "\n" +
               "    password: " + password + "\n" +
               "    newPassword: " + newPassword + "\n" +
               "    authorities: " + authoritiesToString(authorities) + "\n" +
               "    createdDate: " + createdDate +  "\n" +
               "    modifiedDate: " + modifiedDate + "\n" +
               "    nonExpired: " + nonExpired + "\n" +
               "    nonLocked: " + nonLocked + "\n" +
               "    credentialsNonExpired: " + credentialsNonExpired + "\n" +
               "    enabled: " + enabled + "\n" +
               "}";
    }

    private static String authoritiesToString(Set<AuthorityDTO> authorities) {
        final StringBuilder a = new StringBuilder();
        a.append("[");

        if (authorities != null) {
            for (AuthorityDTO authority : authorities) {
                a.append(authority.getAuthorityCode());
                a.append(",");
            }
        }

        a.append("]");

        return a.toString();
    }
}
