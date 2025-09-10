package nechto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Authority implements GrantedAuthority {
    ROLE_USER("user"),
    ROLE_ADMIN("admin"),
    ROLE_OWNER("owner");

    private final String name;

    @Override
    public String getAuthority() {
        return name();
    }
}
