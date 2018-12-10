package site.ownw.authserver.service;

import org.springframework.data.domain.Page;
import site.ownw.authserver.entity.Role;
import site.ownw.authserver.model.request.RoleRequestModel;

/**
 * @author sofior
 * @date 2018/11/12 14:08
 */
public interface RoleService {

    /**
     * 分页查询
     *
     * @param page 第几页，从0开始
     * @param size 页面大小
     * @return 分页信息
     */
    Page<Role> findRoles(Integer page, Integer size);

    /**
     * 新增修改角色
     *
     * @param roleRequestModel 需要新增和修改的角色
     * @return 需要新增和修改后的角色
     */
    Role saveRole(RoleRequestModel roleRequestModel);

    /**
     * 删除角色
     *
     * @param roleId 需要删除的角色的ID
     * @return 被删除的角色
     */
    Role delRole(Long roleId);
}
