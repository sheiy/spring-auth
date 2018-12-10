package site.ownw.authserver.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.ownw.authserver.entity.converter.JpaJSONConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.util.List;

/**
 * @author Sofior
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class PermissionUrl extends BaseEntity {
    private static final long serialVersionUID = 1090170470610098642L;

    /**
     * 资源服务器标识,只有标识和url都匹配才能访问
     */
    private String resourceId;

    /**
     * 允许访问的网址
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JpaJSONConverter.class)
    private List<String> url;

    /**
     * 备注信息
     */
    @Column(name = "`comment`")
    private String comment;

    /**
     * 是否启用
     */
    private Boolean enable;
}
