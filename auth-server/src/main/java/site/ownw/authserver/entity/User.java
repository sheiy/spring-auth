package site.ownw.authserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author sofior
 * @date 2018/8/16 22:38
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    private static final long serialVersionUID = -4486175971928321934L;

    /**
     * 用户自定义名称
     */
    @Pattern(regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]{3,20}$")
    private String nickName;

    /**
     * 注册用户名
     */
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{7,15}$")
    @Column(unique = true, nullable = false, length = 30)
    private String username;

    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 邮箱
     */
    @Email
    @Column(unique = true)
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "1[0-9]{10}")
    @Column(unique = true, length = 11)
    private String phone;

    /**
     * 账号是否可用
     */
    @Column(nullable = false, columnDefinition = "bool default true")
    private Boolean enable;

    /**
     * 用户头像
     */
    @Column
    private String avatarUrl;

    /**
     * 用户角色
     */
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles == null ? Set.of() : Set.copyOf(roles);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

}
