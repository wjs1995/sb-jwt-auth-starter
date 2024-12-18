package cloud.xiaoweiyun.auth.pojo;

import cloud.xiaoweiyun.auth.enums.GenderEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

public class UserEntity extends User {

    public Long Id;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public GenderEnum getGender() {
        return gender;
    }


    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    private GenderEnum gender;

    public UserEntity(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserEntity(String username, Long id) {
        super(username,"", new ArrayList<>());
        this.Id = id;
    }

    public UserEntity(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
