package site.ownw.authserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import site.ownw.authserver.entity.Role;
import site.ownw.authserver.model.request.RoleRequestModel;
import site.ownw.authserver.service.RoleService;

/**
 * @author sofior
 * @date 2018/11/12 14:07
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/findRoles")
    public Page<Role> findRoles(@RequestParam(required = false, defaultValue = "0") Integer page,
                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return roleService.findRoles(page, size);
    }

    @PostMapping("/saveRole")
    public Role saveRole(@RequestBody RoleRequestModel role) {
        return roleService.saveRole(role);
    }

    @DeleteMapping("/{roleId}")
    public Role delRole(@PathVariable Long roleId) {
        return roleService.delRole(roleId);
    }
}
