package site.ownw.authserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * @author Sofior
 */
@Data
@Entity
public class Scope extends BaseEntity {

    private static final long serialVersionUID = 3190659095296513457L;

    /**
     * 英文名称
     */
    private String name;

    /**
     * 前端显示的描述信息
     */
    @Column(name = "`desc`")
    private String desc;

    /**
     * 备注
     */
    @Column(name = "`comment`")
    private String comment;

    /**
     * 允许访问的路径
     */
    @ManyToMany(targetEntity = PermissionUrl.class, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "scope_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_url_id")})
    private List<PermissionUrl> permissionUrls;

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
        Scope scope = (Scope) o;
        return Objects.equals(name, scope.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
