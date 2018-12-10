package site.ownw.authserver.service;

import org.springframework.data.domain.Page;
import site.ownw.authserver.entity.PermissionUrl;

/**
 * @author sofior
 * @date 2018/8/17 00:03
 */
public interface PermissionUrlService {

    /**
     * 分页查询
     *
     * @param page 第几页，从0开始
     * @param size 页面大小
     * @return 分页信息
     */
    Page<PermissionUrl> findPermissionUrls(Integer page, Integer size);

    /**
     * 新增修改权限URL
     *
     * @param permissionUrl 需要新增和修改的权限URL
     * @return 需要新增和修改后的权限URL
     */
    PermissionUrl savePermissionUrl(PermissionUrl permissionUrl);

    /**
     * 删除权限URL
     *
     * @param permissionUrlId 需要删除的权限URL的ID
     * @return 被删除的权限URL
     */
    PermissionUrl delPermissionUrl(Long permissionUrlId);
}
