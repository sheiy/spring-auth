package site.ownw.authserver.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * @author sofior
 * @date 2018/10/25 17:17
 */
@Data
@Entity
public class Role extends BaseEntity implements GrantedAuthority {

    private static final long serialVersionUID = 5371041476000011866L;

    /**
     * 英文名称
     */
    @Column(nullable = false, length = 30)
    private String name;

    /**
     * 中文名称
     */
    @Column(nullable = false, length = 30)
    private String nickName;

    /**
     * 备注
     */
    @Column(name = "`comment`")
    private String comment;

    /**
     * 允许访问的路径
     */
    @ManyToMany(targetEntity = PermissionUrl.class, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_url_id")})
    private List<PermissionUrl> permissionUrls;

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
