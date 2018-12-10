package site.ownw.authserver.model.request;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.util.List;

@Data
public class ClientRequestModel implements Persistable<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所有者
     */
    private String owner;

    /**
     * 授权类型
     */
    private List<String> authorizedGrantTypes = List.of();

    /**
     * scopes
     */
    private List<Long> scopes = List.of();

    /**
     * roles
     */
    private List<Long> roles = List.of();

    /**
     * 回调地址
     */
    private List<String> registeredRedirectUri = List.of();

    /**
     * 用户授权是否自动通过
     */
    private Boolean autoApprove;

    @Override
    public boolean isNew() {
        return getId() == null;
    }
}
