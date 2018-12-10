package site.ownw.authserver.model.request;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.util.List;

@Data
public class UserRequestModel implements Persistable<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户自定义名称
     */
    private String nickName;

    /**
     * 注册用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 账号是否可用
     */
    private Boolean enable;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户角色
     */
    private List<Long> roles = List.of();

    @Override
    public boolean isNew() {
        return getId() == null;
    }
}
