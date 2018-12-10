package site.ownw.authserver.model.request;

import lombok.Data;
import org.springframework.data.domain.Persistable;
import site.ownw.authserver.entity.PermissionUrl;

import javax.persistence.*;
import java.util.List;

@Data
public class ScopeRequestModel implements Persistable<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 英文名称
     */
    private String name;

    /**
     * 前端显示的描述信息
     */
    private String desc;

    /**
     * 备注
     */
    private String comment;

    /**
     * 允许访问的路径
     */
    private List<Long> permissionUrls = List.of();

    @Override
    public boolean isNew() {
        return getId() == null;
    }
}
