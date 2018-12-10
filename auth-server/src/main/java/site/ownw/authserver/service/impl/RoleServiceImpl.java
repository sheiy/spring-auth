package site.ownw.authserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import site.ownw.authserver.entity.Client;
import site.ownw.authserver.entity.Role;
import site.ownw.authserver.model.request.RoleRequestModel;
import site.ownw.authserver.repository.PermissionUrlRepository;
import site.ownw.authserver.repository.RoleRepository;
import site.ownw.authserver.service.RoleService;
import site.ownw.authserver.util.BeanUtils;

import java.util.ArrayList;

/**
 * @author sofior
 * @date 2018/11/12 14:09
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionUrlRepository permissionUrlRepository;

    @Override
    public Page<Role> findRoles(Integer page, Integer size) {
        return roleRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Role saveRole(RoleRequestModel roleRequestModel) {
        Role role;
        if (roleRequestModel.isNew()) {
            role = new Role();
            BeanUtils.copyProperties(roleRequestModel, role);
        } else {
            var optional = roleRepository.findById(roleRequestModel.getId());
            BeanUtils.copyProperties(roleRequestModel, optional.orElseThrow(() -> new RuntimeException("修改失败")), BeanUtils.findNullFields(roleRequestModel));
            role = optional.get();
        }
        role.setPermissionUrls(new ArrayList<>(roleRequestModel.getPermissionUrls().size()));
        for (Long permissionUrlId : roleRequestModel.getPermissionUrls()) {
            permissionUrlRepository.findById(permissionUrlId).ifPresent(permissionUrl -> role.getPermissionUrls().add(permissionUrl));
        }
        return roleRepository.save(role);
    }

    @Override
    public Role delRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("删除失败"));
        roleRepository.deleteById(roleId);
        return role;
    }

}
