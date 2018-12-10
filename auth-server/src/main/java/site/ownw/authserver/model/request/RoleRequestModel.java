package site.ownw.authserver.model.request;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.util.List;

@Data
public class RoleRequestModel implements Persistable<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 英文名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String nickName;

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
