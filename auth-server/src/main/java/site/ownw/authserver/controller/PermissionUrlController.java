package site.ownw.authserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import site.ownw.authserver.entity.PermissionUrl;
import site.ownw.authserver.service.PermissionUrlService;

/**
 * @author Sofior
 */
@RestController
@RequestMapping("/permissionUrl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionUrlController {

    private final PermissionUrlService permissionUrlService;


    @GetMapping("/findPermissionUrls")
    public Page<PermissionUrl> findPermissionUrls(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return permissionUrlService.findPermissionUrls(page, size);
    }

    @PostMapping("/savePermissionUrl")
    public PermissionUrl savePermissionUrl(@RequestBody PermissionUrl permissionUrl) {
        return permissionUrlService.savePermissionUrl(permissionUrl);
    }

    @DeleteMapping("/{permissionUrlId}")
    public PermissionUrl delPermissionUrl(@PathVariable Long permissionUrlId) {
        return permissionUrlService.delPermissionUrl(permissionUrlId);
    }
}
